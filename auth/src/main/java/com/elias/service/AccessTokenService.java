package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.concurrency.RedisDistributeLock;
import com.elias.entity.AccessToken;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.view.AccessTokenView;
import com.elias.repository.AccessTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:06 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final Environment env;
    private final RedisCacheOperator redisCacheOperator;
    private final RedisDistributeLock redisDistributeLock;

    /**
     * 分布式锁在redis中的key
     */
    private static final String LOCK_NAME = "auth:token:lock";

    /**
     * 令牌在redis中的key，第一个占位符标识令牌id
     */
    private static final String REDIS_KEY_DATA_MAPPED_BY_ID = "auth:token:data:%s";


    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository, Environment env,
                              RedisCacheOperator redisCacheOperator) {
        this.accessTokenRepository = accessTokenRepository;
        this.env = env;
        this.redisCacheOperator = redisCacheOperator;
        this.redisDistributeLock = new RedisDistributeLock(LOCK_NAME);
    }

    /**
     * 获取令牌。注意：重复获取不会生成新的令牌，除非令牌已过期，但是会刷新令牌的有效期
     * 调用该方法的地方需要自行判断 ownerId 是否是真实存在于本系统的client或user
     *
     * @param ownerId   令牌的接受者id，可能是一个client，也可能是一个user
     * @param ownerType 接受者的类型
     * @return {@link AccessTokenView}
     */
    public AccessToken getToken(UUID ownerId, AccessToken.OwnerType ownerType) {
        // 获取分布式锁
        redisDistributeLock.acquire();
        AccessToken accessToken = accessTokenRepository.findByOwnerIdAndOwnerType(ownerId, ownerType.getType());
        if (accessToken == null || ttl(accessToken) <= 0) {
            if (accessToken != null) {
                // 删除已过期的令牌
                accessTokenRepository.delete(accessToken);
            }
            accessToken = new AccessToken();
            accessToken.setOwnerId(ownerId);
            accessToken.setOwnerType(ownerType.getType());
            accessToken.setExpire(getTokenExpire(ownerType));
        }
        accessToken = accessTokenRepository.save(accessToken);
        // 手动释放分布式锁
        redisDistributeLock.release();
        return accessToken;
    }


    /**
     * 校验令牌的合法性。只有令牌能找到并且剩余生存时间大于0才是合法的令牌
     *
     * @param token 令牌
     */
    public AccessToken isValid(UUID token) {
        AccessToken accessToken = findById(token);
        if (accessToken == null) {
            throw new RestException(ErrorCode.ACCESS_TOKEN_NOT_FOUND);
        }
        if (ttl(accessToken) <= 0) {
            throw new RestException(ErrorCode.UNAUTHORIZED);
        }
        return accessToken;
    }

    public AccessToken findById(UUID token) {
        String key = String.format(REDIS_KEY_DATA_MAPPED_BY_ID, token);
        AccessToken accessToken = (AccessToken) redisCacheOperator.valueOperations().get(key);
        if (accessToken == null) {
            accessToken = accessTokenRepository.findById(token).orElse(null);
            if (accessToken != null) {
                redisCacheOperator.atomicSetIfAbsentAndExpireRandom(key, accessToken, accessToken.getExpire(), TimeUnit.MILLISECONDS);
            }
        }
        return accessToken;
    }


    /**
     * 返回令牌的有效时间。优先从配置文件中获取，如果配置文件中没有，就使用默认的。单位为毫秒
     *
     * @param ownerType 令牌的类型，{@link AccessToken.OwnerType}
     * @return 令牌的有效期
     */
    private int getTokenExpire(AccessToken.OwnerType ownerType) {
        String configurationItemName;
        if (ownerType.getType() == AccessToken.OwnerType.CLIENT.getType()) {
            configurationItemName = Constants.ENV_CLIENT_TOKEN_EXPIRE_KEY;
        } else if (ownerType.getType() == AccessToken.OwnerType.USER.getType()) {
            configurationItemName = Constants.ENV_USER_TOKEN_EXPIRE_KEY;
        } else {
            throw new RestException(ErrorCode.UNSUPPORTED_TOKEN_TYPE);
        }
        String item = env.getProperty(configurationItemName);
        if (!StringUtils.isEmpty(item)) {
            try {
                return Integer.parseInt(item);
            } catch (NumberFormatException e) {
                log.error("配置项 [{}] 错误: 无法被解析成整数", configurationItemName);
            }
        } else {
            log.warn("找不到配置项 [{}]: 使用默认配置", configurationItemName);
        }
        // 默认客户端令牌有效时长为7天，用户令牌为2小时
        return ownerType.getType() == AccessToken.OwnerType.CLIENT.getType() ? 604800000 : 7200000;
    }

    /**
     * 返回令牌的剩余生存时间
     *
     * @param accessToken 令牌
     * @return 该令牌的剩余生存时间毫秒值
     */
    public static int ttl(AccessToken accessToken) {
        return (int) (accessToken.getUpdateTime().getTime() + accessToken.getExpire() - System.currentTimeMillis());
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 100; i += 2) {
                sum += i;
            }
            System.out.println(Thread.currentThread().getName() + ": sum is " + sum);
        });

        Thread t2 = new Thread(() -> {
            int sum = 0;
            for (int i = 1; i < 100; i += 2) {
                sum += i;
            }
            System.out.println(Thread.currentThread().getName() + ": sum is " + sum);
        });
        t1.start();
        t1.join(); // 等待t1线程执行完毕
        t2.start();
        t2.join(); // 等待t2线程执行完毕
        System.out.println("main thread: end");
    }
}
