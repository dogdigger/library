package com.elias.service;

import com.elias.common.Constants;
import com.elias.common.cache.RedisCacheOperator;
import com.elias.entity.VerifyCode;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.repository.VerifyCodeRepository;
import com.elias.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 11:04 上午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class VerifyCodeService {

    private final VerifyCodeRepository verifyCodeRepository;
    private final RedisCacheOperator redisCacheOperator;

    /**
     * 分布式锁在redis中的key
     */
    private static final String REDIS_KEY_DISTRIBUTED_LOCK = "auth:verify_code:lock";

    public VerifyCodeService(VerifyCodeRepository verifyCodeRepository, RedisCacheOperator redisCacheOperator) {
        this.verifyCodeRepository = verifyCodeRepository;
        this.redisCacheOperator = redisCacheOperator;
    }

    /**
     * 为指定的手机号生成验证码(长度为6，有效时间10分钟)，保证不重复(10分钟之内)。
     * 业务方需要保证手机号码是一个有效的手机号码。
     *
     * @param mobile 手机号码
     * @return 生成的验证码。{@link VerifyCode}
     */
    public VerifyCode createVerifyCode(String mobile) {
        // 生成一个mobile没有使用过的验证码
        String code = CommonUtils.generateRandomNumberString(6);
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setMobile(mobile);
        verifyCode.setExpire(10);
        // 获取分布式锁
        redisCacheOperator.acquireLock(REDIS_KEY_DISTRIBUTED_LOCK, Constants.DISTRIBUTED_LOCK_EXPIRE, TimeUnit.MILLISECONDS);
        VerifyCode v;
        while ((v = verifyCodeRepository.findByMobileAndCode(mobile, code)) != null && ttl(v) > 0) {
            code = CommonUtils.generateRandomNumberString(6);
        }
        verifyCode.setCode(code);
        verifyCode = verifyCodeRepository.save(verifyCode);
        // 释放锁
        redisCacheOperator.delete(REDIS_KEY_DISTRIBUTED_LOCK);
        redisCacheOperator.valueOperations().setIfAbsent(code, verifyCode, 10, TimeUnit.MINUTES);
        log.info("为手机号 [{}] 生成的验证码是 [{}]", mobile, verifyCode);
        return verifyCode;
    }

    /**
     * 判断手机号和验证码的组合是否合法，如果不合法，将抛出异常
     *
     * @param mobile 手机号码
     * @param code   验证码
     */
    public void validateVerifyCode(String mobile, String code) {
        VerifyCode verifyCode = (VerifyCode) redisCacheOperator.valueOperations().get(code);
        if (verifyCode != null) {
            if (!mobile.equals(verifyCode.getMobile())) {
                throw new RestException(ErrorCode.WRONG_VERIFY_CODE);
            }
            return;
        }
        verifyCode = verifyCodeRepository.findByMobileAndCode(mobile, code);
        if (verifyCode == null) {
            throw new RestException(ErrorCode.WRONG_VERIFY_CODE);
        }
        long ttl = ttl(verifyCode);
        if (ttl <= 0) {
            throw new RestException(ErrorCode.EXPIRED_VERIFY_CODE);
        }
        redisCacheOperator.valueOperations().setIfAbsent(code, verifyCode, ttl, TimeUnit.MILLISECONDS);
    }

    /**
     * 返回验证码的剩余有效时间，单位为毫秒
     *
     * @param verifyCode 验证码实体对象
     * @return 剩余有效时间
     */
    private long ttl(VerifyCode verifyCode) {
        long timestamp = System.currentTimeMillis();
        Date dt = CommonUtils.dateAdd(verifyCode.getCreateTime(), TimeUnit.MINUTES, verifyCode.getExpire());
        return timestamp - dt.getTime();
    }

}
