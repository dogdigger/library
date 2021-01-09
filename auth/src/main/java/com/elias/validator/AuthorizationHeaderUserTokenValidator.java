package com.elias.validator;

import com.elias.common.annotation.Validator;
import com.elias.service.AccessTokenService;
import org.springframework.stereotype.Component;

/**
 * @author chengrui
 * <p>create at: 2020/11/28 10:04 上午</p>
 * <p>description: 校验请求中的Authorization请求头，必须满足: Bearer {{userToken}}</p>
 */
@Component
public class AuthorizationHeaderUserTokenValidator implements Validator {
    private final AccessTokenService accessTokenService;

    public AuthorizationHeaderUserTokenValidator(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Override
    public void validate(String value) {
        accessTokenService.isValid(AuthorizationHeaderUtils.validate(value));
    }
}
