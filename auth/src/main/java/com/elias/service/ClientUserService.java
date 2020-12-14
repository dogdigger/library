package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.entity.ClientUser;
import com.elias.entity.User;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.repository.ClientUserRepository;
import com.elias.util.CommonUtils;
import com.elias.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/12/14 5:38 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class ClientUserService {
    private final ClientUserRepository clientUserRepository;
    private final RedisCacheOperator redisCacheOperator;

    /**
     * 分布式锁在redis中的key
     */
    private static final String REDIS_KEY_DISTRIBUTED_LOCK = "auth:client_user:lock";

    /**
     * 数据在redis中的key，第一个占位符标识客户端id，第二个占位符标识用户id
     */
    private static final String REDIS_KEY_DATA = "auth:client_user:%s:%s";

    /**
     * 数据在redis中的有效时间，默认7天
     */
    private static final long REDIS_EXPIRE_TIME = 7 * Constants.ExpireTimeEnum.DAY.getTime();

    public ClientUserService(ClientUserRepository clientUserRepository, RedisCacheOperator redisCacheOperator) {
        this.clientUserRepository = clientUserRepository;
        this.redisCacheOperator = redisCacheOperator;
    }

    // 注册成为指定客户端的用户
    public ClientUser userRegister(UUID clientId, User user) {
        ClientUser clientUser = find(clientId, user.getId());
        if (clientUser != null) {
            throw new RestException(ErrorCode.DUPLICATE_REGISTRATION);
        }
        clientUser = new ClientUser();
        clientUser.setClientId(clientId);
        clientUser.setUserId(user.getId());
        clientUser.setDisplayName(user.getName());
        // 生成指定长度的salt
        String salt = CommonUtils.generateRandomString(Constants.PASSWORD_SALT_LENGTH);
        clientUser.setSalt(salt);
        // 初始密码为一个随机字符串(长度为10) -- 用户第一次必须通过手机验证码来修改密码
        String rawPassword = CommonUtils.generateRandomString(Constants.INITIAL_PASSWORD_LENGTH);
        // 对初始密码进行sha256签名
        String digest = SecurityUtils.digest("SHA3-256", rawPassword);
        // 然后对sha256加密后的结果加盐，再进行一次加密，得到存储到数据库中的密码
        clientUser.setPassword(SecurityUtils.encrypt(digest, salt));

        ClientUser tmp;
        boolean printLog = false;
        // 获取分布式锁
        redisCacheOperator.acquireLock(REDIS_KEY_DISTRIBUTED_LOCK, Constants.DISTRIBUTED_LOCK_EXPIRE, TimeUnit.MILLISECONDS);
        if ((tmp = find(clientId, user.getId())) == null) {
            tmp = clientUserRepository.save(clientUser);
            printLog = true;
        }
        // 释放分布式锁
        redisCacheOperator.delete(REDIS_KEY_DISTRIBUTED_LOCK);

        // 打印日志
        if (printLog) {
            log.info("用户 [{}] 注册成为 [client={}] 的用户，初始密码为 [{}]", user, clientId, rawPassword);
        }
        // 将记录放入redis缓存
        String key = String.format(REDIS_KEY_DATA, clientId, user.getId());
        redisCacheOperator.setIfAbsentAndExpireRandom(key, clientUser, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
        return tmp;
    }

    public ClientUser find(UUID clientId, UUID userId) {
       // 先在Redis中查找
        String key = String.format(REDIS_KEY_DATA, clientId, userId);
        ClientUser clientUser = (ClientUser) redisCacheOperator.valueOperations().get(key);
        if (clientUser == null) {
            clientUser = clientUserRepository.findByClientIdAndUserId(clientId, userId);
            redisCacheOperator.setIfAbsentAndExpireRandom(key, clientUser, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
        }
        return clientUser;
    }
}
