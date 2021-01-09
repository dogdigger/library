package com.elias.api.auth.token;

import com.elias.common.annotation.ValidRequestHeader;
import com.elias.common.Constants;
import com.elias.common.PathDefinition;
import com.elias.entity.AccessToken;
import com.elias.entity.Account;
import com.elias.entity.Client;
import com.elias.entity.User;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.form.user.UserLoginForm;
import com.elias.model.view.AccessTokenView;
import com.elias.response.GenericResponse;
import com.elias.service.*;
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
    private final AccountService accountService;

    public TokenController(AccessTokenService accessTokenService, ClientService clientService,
                           VerifyCodeService verifyCodeService, UserService userService, AccountService accountService) {
        this.accessTokenService = accessTokenService;
        this.clientService = clientService;
        this.verifyCodeService = verifyCodeService;
        this.userService = userService;
        this.accountService = accountService;
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
        Client client = clientService.findById(clientId);
        if (!client.getSecret().equals(arr[1])) {
            throw new RestException(ErrorCode.WRONG_SECRET_OR_PASSWORD);
        }
        // 颁发令牌
        AccessToken accessToken = accessTokenService.getToken(clientId, AccessToken.OwnerType.CLIENT);
        return GenericResponse.ok(AccessTokenView.createFromAccessToken(accessToken));
    }

    @ValidRequestHeader(headerName = Constants.HEADER_AUTHORIZATION, validator = AuthorizationHeaderClientTokenValidator.class)
    @PostMapping("/types/user")
    public GenericResponse<AccessTokenView> userAuth(@RequestBody @Valid UserLoginForm userLoginForm) {
        UserLoginForm.LoginTypeEnum loginTypeEnum = UserLoginForm.LoginTypeEnum.loginTypeEnum(userLoginForm.getType());
        if (loginTypeEnum == null) {
            throw new RestException(ErrorCode.UNSUPPORTED_LOGIN_TYPE);
        }
        User user = null;
        switch (loginTypeEnum) {
            // 验证码登录
            case VERIFY_CODE: {
                // 验证手机号是否注册过
                user = userService.findUserByMobile(userLoginForm.getKey());
                if (user == null) {
                    throw new RestException(ErrorCode.UNREGISTERED_MOBILE);
                }
                // 校验验证码是否正确
                verifyCodeService.validateVerifyCode(userLoginForm.getKey(), userLoginForm.getCode());
                break;
            }
            // 密码模式
            case PASSWORD: {
                // 验证是否是一个真实存在的用户
                user = userService.findUserByMobile(userLoginForm.getKey());
                user = user == null ? userService.findByEmail(userLoginForm.getKey()) : user;
                if (user == null) {
                    throw new RestException(ErrorCode.USER_NOT_FOUND);
                }
                Account account = accountService.findByUserId(user.getId());
                // 找不到账号：抛出业务逻辑错误异常
                if (account == null) {
                    throw new RestException(ErrorCode.BUSINESS_LOGIC_ERROR, "user exist but account can not be found");
                }
                // 验证密码是否正确
                if (!accountService.isPasswordCorrect(account, userLoginForm.getCode())) {
                    throw new RestException(ErrorCode.WRONG_SECRET_OR_PASSWORD, "the password is incorrect");
                }
            }
        }
        // 颁发令牌
        AccessToken accessToken = accessTokenService.getToken(user.getId(), AccessToken.OwnerType.USER);
        return GenericResponse.ok(AccessTokenView.createFromAccessToken(accessToken));
    }
}
