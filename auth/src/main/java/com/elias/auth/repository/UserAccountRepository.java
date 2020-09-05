package com.elias.auth.repository;

import com.elias.auth.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 7:04 下午</p>
 * <p>description: </p>
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    UserAccount findByAccount(String account);
}
