package com.elias.service;

import com.elias.repository.AccountRepository;
import com.elias.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author chengrui
 * <p>create at: 2020/11/25 8:07 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
}
