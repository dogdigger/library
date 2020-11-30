package com.elias.validator;

import com.elias.common.annotation.Validator;
import com.elias.service.AccessTokenService;
import org.springframework.stereotype.Component;

/**
 * @author chengrui
 * <p>create at: 2020/11/27 4:32 下午</p>
 * <p>description: 校验请求中的Authorization请求头，必须满足: Bearer {{clientToken}}</p>
 */
@Component
public class AuthorizationHeaderClientTokenValidator implements Validator {
    private final AccessTokenService accessTokenService;

    public AuthorizationHeaderClientTokenValidator(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Override
    public void validate(String value) {
        accessTokenService.validateClientAccessToken(AuthorizationHeaderUtils.validate(value));
    }
}
