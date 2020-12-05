package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.entity.AccessToken;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.view.AccessTokenView;
import com.elias.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:06 下午</p>
 * <p>description: </p>
 */
@Service
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final Environment env;
    private final RedisCacheOperator redisCacheOperator;

    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository, Environment env,
                              RedisCacheOperator redisCacheOperator) {
        this.accessTokenRepository = accessTokenRepository;
        this.env = env;
        this.redisCacheOperator = redisCacheOperator;
    }

    /**
     * 获取令牌。注意：重复获取不一定会生成新的令牌。
     * 令牌的刷新机制：当令牌的剩余生存时间小于令牌寿命的1/4时会刷新令牌
     *
     * @param ownerId   令牌的接受者id，可能是一个client，也可能是一个user
     * @param ownerType 接受者的类型
     * @return {@link AccessTokenView}
     */
    public AccessTokenView getToken(UUID ownerId, AccessToken.OwnerType ownerType) {
        // 调用该方法的地方，需要校验ownerId是否是一个真实存在的
        String redisKey = Constants.REDIS_KEY_ACCESS_TOKEN;
        // 先查询redis
        AccessToken accessToken = (AccessToken) redisCacheOperator.hashOperations().get(redisKey, ownerId);
        // 如果redis中没有，则查询数据库
        if (accessToken == null) {
            accessToken = accessTokenRepository.findByOwnerIdAndOwnerType(ownerId, ownerType.getType());
            // 如果数据库中也没有，就尝试新建一个
            if (accessToken == null) {
                // 获取分布式锁
                boolean acquired = redisCacheOperator.getDistributedLock();
                // 如果获取成功，则写入数据
                if (acquired) {
                    accessToken = new AccessToken();
                    accessToken.setOwnerId(ownerId);
                    accessToken.setOwnerType(ownerType.getType());
                    accessToken.setExpire(getTokenExpire(ownerType));
                    // 写入数据库
                    accessToken = accessTokenRepository.save(accessToken);
                    // 写入redis
                    redisCacheOperator.hashOperations().put(redisKey, ownerId, accessToken);
                    // 设置过期时间
                    redisCacheOperator.expire(redisKey, );
                }
            }
        }

        if (accessToken != null && ttl(accessToken) < (accessToken.getExpire() >> 2)) {
            // 如果令牌剩余生存时间不足1/4，就删除令牌然后创建一个新的令牌 --- 惰性删除
            accessTokenRepository.delete(accessToken);
            accessToken = null;
        }
        // 创建新的令牌
        if (accessToken == null) {
            accessToken = new AccessToken();
            accessToken.setOwnerId(ownerId);
            accessToken.setOwnerType(ownerType.getType());
            accessToken.setExpire(getTokenExpire(ownerType));
            synchronized (AccessTokenService.class) {
                AccessToken tmp = accessTokenRepository.findByOwnerIdAndOwnerType(ownerId, ownerType.getType());
                accessToken = tmp == null ? accessTokenRepository.save(accessToken) : tmp;
            }
        }
        AccessTokenView view = new AccessTokenView();
        view.setId(accessToken.getId());
        view.setTtl(ttl(accessToken));
        return view;
    }


    /**
     * 校验客户端令牌
     *
     * @param clientAccessToken 客户端访问令牌
     */
    public void validateClientAccessToken(UUID clientAccessToken) {
        validateAccessToken(clientAccessToken, AccessToken.OwnerType.CLIENT.getType());
    }

    /**
     * 校验用户令牌
     *
     * @param userAccessToken 用户访问令牌
     */
    public void validateUserAccessToken(UUID userAccessToken) {
        validateAccessToken(userAccessToken, AccessToken.OwnerType.USER.getType());
    }

    /**
     * 校验令牌的合法性。只有令牌能找到并且剩余生存时间大于0才是合法的令牌
     *
     * @param accessToken 令牌
     */
    private AccessToken validateAccessToken(UUID accessToken) {
        AccessToken token = accessTokenRepository.findById(accessToken).orElse(null);
        if (token == null) {
            throw new RestException(ErrorCode.ACCESS_TOKEN_NOT_FOUND);
        }
        if (ttl(token) <= 0) {
            throw new RestException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
        return token;
    }

    /**
     * 校验令牌的合法性以及令牌的类型
     *
     * @param accessToken 访问令牌
     * @param type        令牌的类型
     * @return 令牌
     */
    private AccessToken validateAccessToken(UUID accessToken, int type) {
        AccessToken token = validateAccessToken(accessToken);
        if (token.getOwnerType() != type) {
            throw new RestException(ErrorCode.UNAUTHORIZED);
        }
        return token;
    }

    /**
     * 决定令牌的有效期。优先从配置文件中获取，如果配置文件中没有，就使用默认的
     *
     * @param ownerType 令牌的类型，{@link AccessToken.OwnerType}
     * @return 令牌的有效期
     */
    private int getTokenExpire(AccessToken.OwnerType ownerType) {
        String expireFromEnv;
        if (ownerType.getType() == AccessToken.OwnerType.CLIENT.getType()) {
            expireFromEnv = env.getProperty(Constants.ENV_CLIENT_TOKEN_EXPIRE_KEY);
        } else if (ownerType.getType() == AccessToken.OwnerType.USER.getType()) {
            expireFromEnv = env.getProperty(Constants.ENV_USER_TOKEN_EXPIRE_KEY);
        } else {
            throw new RestException(ErrorCode.UNSUPPORTED_TOKEN_TYPE);
        }
        if (expireFromEnv != null) {
            try {
                return Integer.parseInt(expireFromEnv);
            } catch (NumberFormatException e) {
                throw new RestException(ErrorCode.WRONG_CONFIGURATION_ITEM);
            }
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
    private int ttl(AccessToken accessToken) {
        return (int) (accessToken.getCreateTime().getTime() + accessToken.getExpire() - new Date().getTime());
    }
}
