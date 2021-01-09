package com.elias.service;

import com.elias.common.cache.RedisCacheOperator;
import com.elias.concurrency.RedisDistributeLock;
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
    private final RedisDistributeLock redisDistributeLock;

    /**
     * 分布式锁在redis中的key
     */
    private static final String LOCK_NAME = "auth:client:lock";

    /**
     * 数据在redis中的key。占位符表示clientId
     */
    private static final String KEY_MAPPED_BY_ID = "auth:client:data:%s";

    /**
     * 数据在redis中的有效时间，默认7天
     */
    private static final long REDIS_EXPIRE_TIME = 60 * 60 * 24 * 7;

    public ClientService(ClientRepository clientRepository, RedisCacheOperator redisCacheOperator) {
        this.clientRepository = clientRepository;
        this.redisCacheOperator = redisCacheOperator;
        this.redisDistributeLock = new RedisDistributeLock(LOCK_NAME);
    }

    public Client createClient(String clientName, String description) {
        Client client = clientRepository.findByName(clientName);
        if (client != null) {
            log.warn("使用 [clientName={}, description={}] 创建客户端失败: 客户端名称已被使用", clientName, description);
            throw new RestException(ErrorCode.DUPLICATE_CLIENT_NAME);
        }
        Client save = new Client();
        save.setName(clientName);
        save.setDescription(description);
        save.setSecret(AppSecretUtils.generate());
        boolean exist = true;
        // 获取分布式锁
        redisDistributeLock.acquire();
        if ((client = clientRepository.findByName(clientName)) == null) {
            client = clientRepository.save(save);
            exist = false;
        }
        // 释放分布式锁
        redisDistributeLock.release();
        if (!exist) {
            log.info("使用 [clientName={}, description={}] 创建客户端成功", clientName, description);
        }
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
        String key = String.format(KEY_MAPPED_BY_ID, clientId);
        Client client = (Client) redisCacheOperator.valueOperations().get(key);
        if (client == null) {
            client = clientRepository.findById(clientId).orElse(null);
            if (client != null) {
                redisCacheOperator.atomicSetIfAbsentAndExpireRandom(key, client, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
            }
        }
        return client;
    }

}
