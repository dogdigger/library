package com.elias.service;

import com.elias.entity.VerifyCode;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.repository.VerifyCodeRepository;
import com.elias.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 11:04 上午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class VerifyCodeService {

    private final VerifyCodeRepository verifyCodeRepository;

    public VerifyCodeService(VerifyCodeRepository verifyCodeRepository) {
        this.verifyCodeRepository = verifyCodeRepository;
    }

    /**
     * 为指定的手机号生成验证码(长度为6，有效时间10分钟)，保证不重复(10分钟之内)。
     * 业务方需要保证手机号码是一个有效的手机号码。
     *
     * @param mobile 手机号码
     * @return 生成的验证码。{@link VerifyCode}
     */
    public VerifyCode createVerifyCode(String mobile) {
        List<VerifyCode> verifyCodes = verifyCodeRepository.findAllByMobile(mobile).stream().filter(r -> !isVerifyCodeExpired(r)).collect(Collectors.toList());
        String code = CommonUtils.generateRandomNumberString(6);
        if (!verifyCodes.isEmpty()) {
            // 死循环的方式来保证生成的验证码不和之前的重复
            while (true) {
                boolean duplicate = false;
                for (VerifyCode verifyCode : verifyCodes) {
                    if (verifyCode.getCode().equals(code)) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    break;
                }
                code = CommonUtils.generateRandomNumberString(6);
            }
        }
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setMobile(mobile);
        verifyCode.setCode(code);
        verifyCode.setExpire(10);
        return verifyCodeRepository.save(verifyCode);
    }

    /**
     * 判断手机号和验证码的组合是否合法，如果不合法，将抛出异常
     *
     * @param mobile 手机号码
     * @param code   验证码
     */
    public void validateVerifyCode(String mobile, String code) {
        VerifyCode verifyCode = verifyCodeRepository.findByMobileAndCode(mobile, code);
        if (verifyCode == null) {
            throw new RestException(ErrorCode.VERIFY_CODE_NOT_FOUND);
        }
        if (isVerifyCodeExpired(verifyCode)) {
            throw new RestException(ErrorCode.EXPIRED_VERIFY_CODE);
        }
    }

    /**
     * 判断验证码是否已过期
     *
     * @param verifyCode 验证码实体对象
     * @return 是否已过期
     */
    private boolean isVerifyCodeExpired(VerifyCode verifyCode) {
        return CommonUtils.dateAdd(verifyCode.getCreateTime(), TimeUnit.MINUTES, verifyCode.getExpire()).before(new Date());
    }

}
