package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.entity.Account;
import com.elias.entity.User;
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

    /**
     * 分布式锁在redis中的key
     */
    private static final String REDIS_KEY_DISTRIBUTED_LOCK = "auth:account:lock";

    /**
     * 数据在redis中的key
     */
    private static final String REDIS_KEY_DATA = "auth:account:data";

    public AccountService(AccountRepository accountRepository, RedisCacheOperator redisCacheOperator) {
        this.accountRepository = accountRepository;
        this.redisCacheOperator = redisCacheOperator;
    }

    public Account createAccount(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user can not be null");
        }
        // 先查询redis
        Account account = (Account) redisCacheOperator.hashOperations().get(REDIS_KEY_DATA, user.getId());
        if (account == null) {
            account = accountRepository.findByUserId(user.getId());
            if (account == null) {
                String randomPassword = null;
                // 获取分布式锁
                redisCacheOperator.acquireLock(REDIS_KEY_DISTRIBUTED_LOCK, Constants.DISTRIBUTED_LOCK_EXPIRE, TimeUnit.MILLISECONDS);
                account = accountRepository.findByUserId(user.getId());
                if (account == null) {
                    account = new Account();
                    account.setUserId(user.getId());
                    // 生成指定长度的salt
                    String salt = CommonUtils.generateRandomString(Constants.PASSWORD_SALT_LENGTH);
                    account.setSalt(salt);
                    // 初始密码为一个随机字符串(长度为10) -- 用户第一次必须通过手机验证码来修改密码
                    randomPassword = CommonUtils.generateRandomString(Constants.INITIAL_PASSWORD_LENGTH);
                    // 对初始密码进行sha256加密
                    String rawPassword = SecurityUtils.digest("SHA3-256", randomPassword);
                    // 然后对sha256加密后的结果加盐，再进行一次加密，得到存储到数据库中的密码
                    account.setPassword(SecurityUtils.encrypt(rawPassword, salt));
                    // 保存
                    account = accountRepository.save(account);
                }
                // 释放分布式锁
                redisCacheOperator.delete(REDIS_KEY_DISTRIBUTED_LOCK);
                if (randomPassword != null) {
                    log.info("为用户 [{}] 创建了账号 [{}], 初始密码是 [{}]", user.getId(), account.getId(), randomPassword);
                }
            }
            // 存入redis
            redisCacheOperator.hashOperations().put(REDIS_KEY_DATA, user.getId(), account);
        }
        return account;
    }

    public Account findByUserId(UUID userId) {
        Account account = (Account) redisCacheOperator.hashOperations().get(REDIS_KEY_DATA, userId);
        if (account == null) {
            account = accountRepository.findByUserId(userId);
            if (account != null) {
                redisCacheOperator.hashOperations().put(REDIS_KEY_DATA, userId, account);
            }
        }
        return accountRepository.findByUserId(userId);
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
