package com.elias.service;

import com.elias.config.Constants;
import com.elias.entity.Account;
import com.elias.entity.User;
import com.elias.repository.AccountRepository;
import com.elias.util.CommonUtils;
import com.elias.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 2:34 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user can not be null");
        }
        Account account = accountRepository.findByUserId(user.getId());
        if (account == null) {
            account = new Account();
            account.setUserId(user.getId());
            // 生成指定长度的salt
            String salt = CommonUtils.generateRandomString(Constants.PASSWORD_SALT_LENGTH);
            account.setSalt(salt);
            // 初始密码为一个随机字符串(长度为10) -- 用户第一次必须通过手机验证码来修改密码
            String randomPassword = CommonUtils.generateRandomString(Constants.INITIAL_PASSWORD_LENGTH);
            // 对初始密码进行sha256加密
            String rawPassword = SecurityUtils.digest("SHA3-256", randomPassword);
            // 然后对sha256加密后的结果加盐，再进行一次加密，得到存储到数据库中的密码
            account.setPassword(SecurityUtils.encrypt(rawPassword, salt));
            account = accountRepository.save(account);
            log.info("为用户 [{}] 设置的初始密码是 [{}]", user.getId(), randomPassword);
        }
        return account;
    }

    public Account findByUserId(UUID userId) {
        return accountRepository.findByUserId(userId);
    }
}
