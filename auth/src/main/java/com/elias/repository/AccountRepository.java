package com.elias.repository;

import com.elias.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/25 8:07 下午</p>
 * <p>description: </p>
 */
public interface AccountRepository extends JpaRepository<Account, UUID> {
    /**
     * 根据用户id来查找用户的账号信息
     *
     * @param userId 用户id
     * @return Account
     */
    Account findByUserId(UUID userId);
}
