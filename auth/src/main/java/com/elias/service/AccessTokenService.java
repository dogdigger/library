package com.elias.service;

import com.elias.entity.AccessToken;
import com.elias.entity.User;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.form.UserLoginByAccountForm;
import com.elias.repository.AccessTokenRepository;
import com.elias.repository.ClientRepository;
import com.elias.repository.UserRepository;
import com.elias.view.AccessTokenView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:06 下午</p>
 * <p>description: </p>
 */
@Service
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    @Value("${com.elias.auth.token.survive}")
    private Integer defaultTokenSurvivalTime;

    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository, UserRepository userRepository, ClientRepository clientRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * 访问令牌是否在有效期内
     *
     * @param accessToken 访问令牌实体对象, {@link AccessToken}
     * @return 布尔值
     */
    private boolean isAccessTokenSurvival(AccessToken accessToken) {
        return accessToken.getCreatedDate().getTime() + accessToken.getSurvivalTime() > System.currentTimeMillis();
    }

    private AccessTokenView accessTokenToView(AccessToken accessToken) {
        AccessTokenView tokenView = new AccessTokenView();
        BeanUtils.copyProperties(accessToken, tokenView);
        tokenView.setToken(accessToken.getId());
        return tokenView;
    }

    // TODO aop + redis
    public AccessTokenView userLoginByAccount(UserLoginByAccountForm userLoginByAccountForm) {
        User user = userRepository.findByAccount(userLoginByAccountForm.getUserName());
        if (user == null) {
            throw new RestException(ErrorCode.USER_NOT_FOUND);
        }
        if (!user.getEnable()) {
            throw new RestException(ErrorCode.USER_DISABLED);
        }
        if (!user.getPwdSalt().equals(userLoginByAccountForm.getUserPwd())) {
            throw new RestException(ErrorCode.WRONG_PASSWORD);
        }
        return createToken(user.getId(), false, null);
    }

    public AccessTokenView clientLogin(String authorizationHeader) {
        return null;
    }

    public AccessTokenView createToken(UUID ownerId, boolean isClient, Integer expiresIn) {
        AccessToken accessToken = accessTokenRepository.findByOwnerId(ownerId);
        if (accessToken == null) {
            // 创建新的令牌
            accessToken = new AccessToken();
            accessToken.setIsClientToken(isClient);
            accessToken.setCreatedDate(new Date());
            accessToken.setOwnerId(ownerId);
            accessToken.setSurvivalTime(expiresIn == null ? defaultTokenSurvivalTime : expiresIn);
        } else if (!isAccessTokenSurvival(accessToken)) {
            // 更新令牌
            accessToken.setCreatedDate(new Date());
        }
        return accessTokenToView(accessTokenRepository.save(accessToken));
    }
}
