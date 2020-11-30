package com.elias.api.auth.token;

import com.elias.common.annotation.ValidRequestHeader;
import com.elias.config.Constants;
import com.elias.config.PathDefinition;
import com.elias.entity.AccessToken;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.form.user.UserLoginForm;
import com.elias.model.view.AccessTokenView;
import com.elias.response.GenericResponse;
import com.elias.service.AccessTokenService;
import com.elias.service.ClientService;
import com.elias.service.UserService;
import com.elias.service.VerifyCodeService;
import com.elias.util.SecurityUtils;
import com.elias.validator.AuthorizationHeaderClientTokenValidator;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/20 10:51 上午</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping(PathDefinition.URI_API_TOKEN)
public class TokenController {
    private final AccessTokenService accessTokenService;
    private final ClientService clientService;
    private final VerifyCodeService verifyCodeService;
    private final UserService userService;

    public TokenController(AccessTokenService accessTokenService, ClientService clientService,
                           VerifyCodeService verifyCodeService, UserService userService) {
        this.accessTokenService = accessTokenService;
        this.clientService = clientService;
        this.verifyCodeService = verifyCodeService;
        this.userService = userService;
    }

    /**
     * 获取客户端令牌
     *
     * @param authorizationHeader Authorization请求头，组成应该是: Basic base64(clientId:secret)
     * @return {@link GenericResponse<AccessTokenView>}
     */
    @PostMapping("/types/client")
    public GenericResponse<AccessTokenView> clientAuth(@RequestHeader(name = Constants.HEADER_AUTHORIZATION) String authorizationHeader) {
        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(Constants.HEADER_AUTHORIZATION_BASIC_PREFIX)) {
            throw new RestException(ErrorCode.WRONG_AUTHORIZATION_HEADER);
        }
        String s = SecurityUtils.decodeToBase64(authorizationHeader.substring(Constants.HEADER_AUTHORIZATION_BASIC_PREFIX.length()));
        String[] arr = s.split(":");
        if (arr.length != 2) {
            throw new RestException(ErrorCode.WRONG_AUTHORIZATION_HEADER);
        }
        UUID clientId;
        try {
            clientId = UUID.fromString(arr[0]);
        } catch (Exception e) {
            throw new RestException(ErrorCode.WRONG_AUTHORIZATION_HEADER);
        }
        // 验证clientId和secret是否匹配
        clientService.validateClient(clientId, arr[1]);
        // 颁发令牌
        return new GenericResponse<>(accessTokenService.getToken(clientId, AccessToken.OwnerType.CLIENT));
    }

    @ValidRequestHeader(headerName = Constants.HEADER_AUTHORIZATION, validator = AuthorizationHeaderClientTokenValidator.class)
    @PostMapping("/types/user")
    public GenericResponse<AccessTokenView> userAuth(@RequestBody @Valid UserLoginForm userLoginForm) {
        // 1、验证登录方式
        if (!UserLoginForm.LoginTypeEnum.isSupported(userLoginForm.getType())) {
            throw new RestException(ErrorCode.UNSUPPORTED_LOGIN_TYPE);
        }
        // 2、如果是验证码登录
        if (userLoginForm.getType() == UserLoginForm.LoginTypeEnum.VERIFY_CODE.getType()) {
            verifyCodeService
        }

        return null;
    }
}
