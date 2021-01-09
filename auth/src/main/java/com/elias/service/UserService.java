package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.concurrency.RedisDistributeLock;
import com.elias.entity.User;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.form.user.UserRegistrationForm;
import com.elias.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/11/25 8:07 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RedisCacheOperator redisCacheOperator;
    private final RedisDistributeLock redisDistributeLock;

    /**
     * 分布式锁在redis中的key
     */
    private static final String LOCK_NAME = "auth:user:lock";

    /**
     * 用户数据在redis中的key: ID映射
     */
    private static final String REDIS_KEY_MAPPED_BY_ID = "auth:user:id:%s";

    /**
     * 用户数据在redis中的key: mobile映射
     */
    private static final String REDIS_KEY_MAPPED_BY_MOBILE = "auth:user:mobile:%s";

    /**
     * 用户数据在redis中的key: email映射
     */
    private static final String REDIS_KEY_MAPPED_BY_EMAIL = "auth:user:email:%s";

    /**
     * 数据在redis中的有效时间，单位为秒。实际上还会在这个值的基础上再加一个随机值
     */
    private static final long REDIS_EXPIRE_TIME = 7 * Constants.ExpireTimeEnum.DAY.getTime();

    public UserService(UserRepository userRepository, RedisCacheOperator redisCacheOperator) {
        this.userRepository = userRepository;
        this.redisCacheOperator = redisCacheOperator;
        this.redisDistributeLock = new RedisDistributeLock(LOCK_NAME);
    }

    /**
     * 创建用户
     *
     * @param userRegistrationForm 封装用户注册必填的信息
     */
    public User createUser(UserRegistrationForm userRegistrationForm) {
        String mobile = userRegistrationForm.getMobile(), email = userRegistrationForm.getEmail();
        User saveUser = new User();
        BeanUtils.copyProperties(userRegistrationForm, saveUser);
        if (findUserByMobile(mobile) != null) {
            throw new RestException(ErrorCode.REGISTERED_MOBILE);
        }
        if (!StringUtils.isEmpty(email) && findByEmail(email) != null) {
            throw new RestException(ErrorCode.REGISTERED_EMAIL);
        }

        User user;
        ErrorCode errorCode = null;
        // 获取分布式锁
        redisDistributeLock.acquire();
        user = findUserByMobile(mobile);
        if (user == null) {
            if (!StringUtils.isEmpty(email) && findByEmail(email) != null) {
                errorCode = ErrorCode.REGISTERED_EMAIL;
            }
            user = userRepository.save(saveUser);
        }else {
            errorCode = ErrorCode.REGISTERED_MOBILE;
        }
        // 释放分布式锁
        redisDistributeLock.release();
        if (errorCode != null) {
            throw new RestException(errorCode);
        }
        return user;
    }

    public User findUserById(UUID userId) {
        String key = String.format(REDIS_KEY_MAPPED_BY_ID, userId);
        User user = (User) redisCacheOperator.valueOperations().get(key);
        if (user == null) {
            user = userRepository.findById(userId);
            writeToCache(key, user);
        }
        return user;
    }

    public User findUserByMobile(String mobile) {
        String key = String.format(REDIS_KEY_MAPPED_BY_MOBILE, mobile);
        User user = (User) redisCacheOperator.valueOperations().get(key);
        if (user == null) {
            user = userRepository.findByMobile(mobile);
            writeToCache(key, user);
        }
        return user;
    }

    public User findByEmail(String email) {
        String key = String.format(REDIS_KEY_MAPPED_BY_EMAIL, email);
        User user = (User) redisCacheOperator.valueOperations().get(key);
        if (user == null) {
            user = userRepository.findByEmail(email);
            writeToCache(key, user);
        }
        return user;
    }

    /**
     * 将数据放入Redis缓存
     *
     * @param key  缓存的key
     * @param user 数据
     */
    private void writeToCache(String key, User user) {
        if (user != null) {
            redisCacheOperator.atomicSetIfAbsentAndExpireRandom(key, user, REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
        }
    }
}
