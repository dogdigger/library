package com.elias.repository;

import com.elias.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 11:03 上午</p>
 * <p>description: </p>
 */
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {
    List<VerifyCode> findAllByMobile(String mobile);

    VerifyCode findByMobileAndCode(String mobile, String code);
}
