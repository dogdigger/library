package com.elias.service;

import com.elias.entity.User;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.form.user.UserRegistrationForm;
import com.elias.repository.AccountRepository;
import com.elias.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chengrui
 * <p>create at: 2020/11/25 8:07 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 创建用户
     *
     * @param userRegistrationForm  封装用户注册必填的信息
     */
    public User createUser(UserRegistrationForm userRegistrationForm) {
        User user = userRepository.findByMobile(userRegistrationForm.getMobile());
        if (user == null) {
            user = new User();
            BeanUtils.copyProperties(userRegistrationForm, user);
            // 这里使用同步来避免并发写入时出现冲突
            synchronized (UserService.class) {
                User tmp = userRepository.findByMobile(userRegistrationForm.getMobile());
                if (tmp == null) {
                    // 创建 User 记录
                    user = userRepository.save(user);
                    log.info("新增一个用户: [{}]", user);
                }else {
                    user = tmp;
                }
            }
        }
        return user;
    }

    public User findUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    public User findUserByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }
}
