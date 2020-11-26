package com.elias.repository;

import com.elias.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 7:04 下午</p>
 * <p>description: </p>
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(UUID id);

    @Query(value = "select u from User u where u.account = :userName or u.mobile = :userName", nativeQuery = true)
    User findByAccountOrMobile(@Param("userName") String userName);

    User findByAccount(String account);

    User findByMobile(String mobile);

    User findByEmail(String email);
}
