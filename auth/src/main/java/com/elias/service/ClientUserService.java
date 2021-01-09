package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.concurrency.RedisDistributeLock;
import com.elias.entity.ClientUser;
import com.elias.entity.User;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.repository.ClientUserRepository;
import com.elias.util.CommonUtils;
import com.elias.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    private final RedisDistributeLock redisDistributeLock;

    /**
     * 分布式锁在redis中的key
     */
    private static final String LOCK_NAME = "auth:client_user:lock";

    /**
     * 数据在redis中的key，占位符标识表示clientUserId
     */
    private static final String REDIS_KEY_DATA = "auth:client_user:%s";

    /**
     * 数据在redis中的有效时间，默认7天
     */
    private static final long REDIS_EXPIRE_TIME = 7 * Constants.ExpireTimeEnum.DAY.getTime();

    @Autowired
    public ClientUserService(ClientUserRepository clientUserRepository,
                             RedisCacheOperator redisCacheOperator) {
        this.clientUserRepository = clientUserRepository;
        this.redisCacheOperator = redisCacheOperator;
        this.redisDistributeLock = new RedisDistributeLock(LOCK_NAME);
    }

    /**
     * 注册成为指定客户端的用户
     * @param clientId
     * @param userId
     * @param displayName
     * @param avatar
     * @return
     */
    public ClientUser createClientUser(UUID clientId, UUID userId, String displayName, String avatar) {
        if (clientId == null || userId == null) {
            throw new IllegalArgumentException("参数clientId和userId不能为null");
        }
        ClientUser clientUser = find(clientId, userId);
        if (clientUser != null) {
            throw new RestException(ErrorCode.CREATE_DUPLICATE_CLIENT_USER);
        }
        ClientUser save = new ClientUser();
        save.setClientId(clientId);
        save.setUserId(userId);
        if (!StringUtils.isEmpty(displayName)) {
            save.setDisplayName(displayName);
        }
        if (!StringUtils.isEmpty(avatar)) {
            save.setAvatar(avatar);
        }

        ErrorCode errorCode = null;
        // 获取分布式锁
        redisDistributeLock.acquire();
        clientUser = find(clientId, userId);
        if (clientUser == null) {
            clientUser = clientUserRepository.save(save);
        }else {
            errorCode = ErrorCode.CREATE_DUPLICATE_CLIENT_USER;
        }
        // 释放分布式锁
        redisDistributeLock.release();
        if (errorCode != null) {
            throw new RestException(errorCode);
        }
        log.info("userId={}注册成为clientId={}的用户, displayName={}, avatar={}", userId, clientId, displayName, avatar);
        return clientUser;
    }

    public ClientUser find(UUID clientId, UUID userId) {
        ClientUser clientUser = clientUserRepository.findByClientIdAndUserId(clientId, userId);
        if (clientUser != null) {
            String key = String.format(REDIS_KEY_DATA, clientUser.getId());
            redisCacheOperator.atomicSetIfAbsentAndExpireRandom(key, clientUser, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        return clientUser;
    }

    public ClientUser findByClientUserId(UUID clientUserId) {
        String key = String.format(REDIS_KEY_DATA, clientUserId);
        ClientUser clientUser = (ClientUser) redisCacheOperator.valueOperations().get(key);
        if (clientUser == null) {
            clientUser = clientUserRepository.findById(clientUserId).orElse(null);
            if (clientUser != null) {
                redisCacheOperator.atomicSetIfAbsentAndExpireRandom(key, clientUser, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
            }
        }
        return clientUser;
    }
}
