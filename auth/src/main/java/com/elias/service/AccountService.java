package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.concurrency.RedisDistributeLock;
import com.elias.entity.Account;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.repository.AccountRepository;
import com.elias.util.CommonUtils;
import com.elias.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 2:34 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final RedisCacheOperator redisCacheOperator;
    private final RedisDistributeLock redisDistributeLock;

    /**
     * 数据在redis中的有效时间，默认为7天
     */
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 分布式锁在redis中的key
     */
    private static final String LOCK_NAME = "auth:account:lock";

    /**
     * 数据在redis中的key。占位符表示clientUserId
     */
    private static final String KEY_MAPPED_BY_CLIENT_USERID = "auth:account:data:%s";

    public AccountService(AccountRepository accountRepository, RedisCacheOperator redisCacheOperator) {
        this.accountRepository = accountRepository;
        this.redisCacheOperator = redisCacheOperator;
        this.redisDistributeLock = new RedisDistributeLock(LOCK_NAME);
    }

    public Account createAccount(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId `userId` can not be null");
        }
        Account account = findByUserId(userId);
        if (account != null) {
            log.warn("userId={} 的账号已存在", account.getUserId());
            throw new RestException(ErrorCode.CREATE_DUPLICATE_ACCOUNT);
        }
        // 待保存的记录
        Account save = new Account();
        save.setUserId(userId);
        // 生成指定长度的salt
        String salt = CommonUtils.generateRandomString(Constants.PASSWORD_SALT_LENGTH);
        save.setSalt(salt);
        // 初始密码为一个随机字符串(长度为10) -- 用户第一次必须通过手机验证码来修改密码
        String randomPassword = CommonUtils.generateRandomString(Constants.INITIAL_PASSWORD_LENGTH);
        // 对初始密码进行sha256加密
        String rawPassword = SecurityUtils.digest("SHA3-256", randomPassword);
        // 然后对sha256加密后的结果加盐，再进行一次加密，得到存储到数据库中的密码
        save.setPassword(SecurityUtils.encrypt(rawPassword, salt));
        ErrorCode errorCode = null;
        // 获取分布式锁
        redisDistributeLock.acquire();
        account = findByUserId(userId);
        if (account == null) {
            // 保存记录
            account = accountRepository.save(save);
        }else {
            log.warn("userId={} 的账号已存在", account.getUserId());
            errorCode = ErrorCode.CREATE_DUPLICATE_ACCOUNT;
        }
        // 释放分布式锁
        redisDistributeLock.release();
        if (errorCode != null) {
            throw new RestException(errorCode);
        }
        // 打印日志
        log.info("为用户 [{}] 创建了账号 [{}], 初始密码是 [{}]", userId, account.getId(), randomPassword);
        return account;
    }

    /**
     * 根据userId来查找账号
     *
     * @param userId userId
     * @return Account
     */
    public Account findByUserId(UUID userId) {
        String key = String.format(KEY_MAPPED_BY_CLIENT_USERID, userId);
        Account account = (Account) redisCacheOperator.valueOperations().get(key);
        if (account == null) {
            account = accountRepository.findByUserId(userId);
            if (account != null) {
                redisCacheOperator.atomicSetIfAbsentAndExpireRandom(key, account, EXPIRE_TIME, TimeUnit.MILLISECONDS);
            }
        }
        return account;
    }

    /**
     * 验证密码是否正确
     *
     * @param account     account实体对象
     * @param shaPassword 原始密码sha256的结果
     * @return boolean
     */
    public boolean isPasswordCorrect(Account account, String shaPassword) {
        return SecurityUtils.encrypt(shaPassword, account.getSalt()).equals(account.getPassword());
    }
}
