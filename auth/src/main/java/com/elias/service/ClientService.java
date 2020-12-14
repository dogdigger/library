package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.entity.Client;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.repository.ClientRepository;
import com.elias.util.AppSecretUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/11/3 8:21 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final RedisCacheOperator redisCacheOperator;

    /**
     * 分布式锁在redis中的key
     */
    private static final String REDIS_KEY_DISTRIBUTED_LOCK = "auth:client:lock";

    /**
     * 数据在redis中的key
     */
    private static final String REDIS_KEY_DATA_MAPPED_BY_ID = "auth:client:id:%s";

    /**
     * 数据在redis中的有效时间，默认7天
     */
    private static final long REDIS_EXPIRE_TIME = 60 * 60 * 24 * 7;

    public ClientService(ClientRepository clientRepository, RedisCacheOperator redisCacheOperator) {
        this.clientRepository = clientRepository;
        this.redisCacheOperator = redisCacheOperator;
    }

    public Client createClient(String clientName, String description) {
        ErrorCode errorCode = null;
        // 获取分布式锁
        redisCacheOperator.acquireLock(REDIS_KEY_DISTRIBUTED_LOCK, Constants.DISTRIBUTED_LOCK_EXPIRE, TimeUnit.MILLISECONDS);
        Client client = clientRepository.findByName(clientName);
        if (client != null) {
            errorCode = ErrorCode.DUPLICATE_CLIENT_NAME;
        }
        if (errorCode == null) {
            client = new Client();
            client.setName(clientName);
            client.setDescription(description);
            client.setSecret(AppSecretUtils.generate());
            client = clientRepository.save(client);
        }
        // 释放分布式锁
        redisCacheOperator.delete(REDIS_KEY_DISTRIBUTED_LOCK);
        if (errorCode != null) {
            log.warn("使用 [clientName={}, description={}] 创建客户端失败: 客户端名称已被使用", clientName, description);
            throw new RestException(errorCode);
        }
        log.info("使用 [clientName={}, description={}] 创建客户端成功", clientName, description);
        // 将数据放入redis中
        String key = String.format(REDIS_KEY_DATA_MAPPED_BY_ID, client.getId());
        redisCacheOperator.setIfAbsentAndExpireRandom(key, client, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
        return client;
    }

    /**
     * 根据clientId查找对应的client实体
     *
     * @param clientId clientId
     * @return {@link Client} or {@code null}
     */
    public Client findById(UUID clientId) {
        // 先在redis中查询
        String key = String.format(REDIS_KEY_DATA_MAPPED_BY_ID, clientId);
        Client client = (Client) redisCacheOperator.valueOperations().get(key);
        if (client == null) {
            client = clientRepository.findById(clientId).orElse(null);
            if (client == null) {
                throw new RestException(ErrorCode.CLIENT_NOT_FOUND);
            }
            // 将数据放入redis
            redisCacheOperator.setIfAbsentAndExpireRandom(key, client, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        return client;
    }

}
