package com.elias.api.auth.token;

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
import com.elias.util.SecurityUtils;
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
@RequestMapping(PathDefinition.URI_API_AUTH)
public class TokenController {
    private final AccessTokenService accessTokenService;
    private final ClientService clientService;

    public TokenController(AccessTokenService accessTokenService, ClientService clientService) {
        this.accessTokenService = accessTokenService;
        this.clientService = clientService;
    }

    /**
     * 获取客户端令牌
     *
     * @param authorizationHeader Authorization请求头，组成应该是: Basic base64(clientId:secret)
     * @return {@link GenericResponse<AccessTokenView>}
     */
    @PostMapping("/client")
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

    @PostMapping("/user")
    public GenericResponse<AccessTokenView> userAuth(@RequestBody @Valid UserLoginForm userLoginForm,
                         @RequestHeader(name = Constants.HEADER_AUTHORIZATION) String authorizationHeader) {
        UUID clientToken = SecurityUtils.getClientToken(authorizationHeader);
        // 校验clientToken是否合法
        accessTokenService.validateAccessToken(clientToken);
        return null;
    }
}
