package com.elias.auth.service;

import com.elias.auth.config.Constants;
import com.elias.auth.entity.AccessToken;
import com.elias.auth.entity.UserAccount;
import com.elias.auth.exception.ErrorCode;
import com.elias.auth.exception.RestException;
import com.elias.auth.form.UserNameAndPasswordLoginForm;
import com.elias.auth.repository.AccessTokenRepository;
import com.elias.auth.repository.UserAccountRepository;
import com.elias.auth.util.DateUtils;
import com.elias.auth.util.UUIDUtils;
import com.elias.auth.view.AccessTokenView;
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
    private final UserAccountRepository userAccountRepository;
    @Value("${com.elias.auth.token.survive}")
    private Integer tokenSurviveTime;
    @Value("${com.elias.auth.token.leeway}")
    private Integer leeway;

    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository, UserAccountRepository userAccountRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * 未指定用户颁发令牌
     *
     * @param userId 用户id
     * @return {@link AccessToken}
     */
    private AccessToken generateAccessToken(Integer userId) {
        UUID token = UUIDUtils.randomUUID();
        AccessToken accessToken = new AccessToken();
        accessToken.setCreateTime(new Date());
        accessToken.setSurviveTime(tokenSurviveTime);
        accessToken.setToken(token);
        accessToken.setUserId(userId);
        accessToken.setDisabled(false);
        return accessToken;
    }

    /**
     * 判断令牌是否还有效
     *
     * @param accessToken 令牌
     * @return boolean
     */
    private boolean isAccessTokenSurvive(AccessToken accessToken) {
        long now = DateUtils.timestampNow();
        long createTime = DateUtils.timestamp(accessToken.getCreateTime());
        return createTime + accessToken.getSurviveTime() < now - leeway;
    }

    /**
     * 通过用户名和密码来获取访问令牌。一天之内密码最多只能输错{@code Constants.MAX_WRONG_TIMES_ONE_DAY}规定的次数，
     * 超过这个次数，账号将被锁定。重复登录不会重新生成令牌，只会刷新令牌的有效时间。
     *
     * @param loginForm 封装账号和密码
     * @return {@link AccessTokenView}
     */
    public AccessTokenView getTokenByAccountAndPassword(UserNameAndPasswordLoginForm loginForm) {
        UserAccount userAccount = userAccountRepository.findByAccount(loginForm.getUserAccount());
        // 不存在的用户
        if (userAccount == null) {
            throw new RestException(ErrorCode.RESOURCE_NOT_FOUND, "wrong account");
        }
        // 账号已被禁用
        if (!userAccount.isEnabled()) {
            throw new RestException(ErrorCode.ACCOUNT_DISABLED, "the account has been disabled");
        }
        // 密码输错次数已达今日上限
        if (userAccount.getErrorCount().equals(Constants.MAX_WRONG_TIMES_ONE_DAY) && DateUtils.isSameDay(new Date(), userAccount.getUpdateTime())) {
            throw new RestException(ErrorCode.ACCOUNT_LOCKED, "your account has been locked, please retry tomorrow");
        }
        // 如果密码不正确
        if (!userAccount.getPassword().equals(loginForm.getUserPassword())) {
            int errCount = DateUtils.isSameDay(new Date(), userAccount.getUpdateTime()) ? userAccount.getErrorCount() : 0;
            userAccount.setErrorCount(errCount + 1);
            userAccount.setUpdateTime(new Date());
            userAccount = userAccountRepository.save(userAccount);
            int remainChance = Constants.MAX_WRONG_TIMES_ONE_DAY - userAccount.getErrorCount();
            if (remainChance != 0) {
                throw new RestException(ErrorCode.OPERATION_WARNING, "wrong password, you have " + remainChance + " times to retry today");
            } else {
                throw new RestException(ErrorCode.ACCOUNT_LOCKED);
            }
        } else {
            AccessToken accessToken = accessTokenRepository.findByUserId(userAccount.getId());
            // 第一次登录，颁发新令牌
            if (accessToken == null) {
                accessToken = generateAccessToken(userAccount.getId());
            } else {
                // 如果令牌已被设置为销毁，则重新启用令牌
                if (accessToken.getDisabled()) {
                    accessToken.setDisabled(false);
                }
                // 刷新令牌的创建时间为当前时间
                accessToken.setCreateTime(new Date());
            }
            accessTokenRepository.save(accessToken);
            AccessTokenView tokenView = new AccessTokenView();
            BeanUtils.copyProperties(accessToken, tokenView);
            return tokenView;
        }
    }

    /**
     * 销毁令牌
     *
     * @param token 要销毁的令牌
     */
    public void disableToken(UUID token) {
        if (token == null) {
            throw new RestException(ErrorCode.RESOURCE_NOT_FOUND, "can not find credentials");
        }
        AccessToken accessToken = accessTokenRepository.findByToken(token);
        // 如果不是本系统颁发的令牌
        if (accessToken == null) {
            throw new RestException(ErrorCode.UNAUTHORIZED, "invalid token, it can not be found");
        }
        // 如果令牌已被销毁
        if (accessToken.getDisabled()) {
            throw new RestException(ErrorCode.UNAUTHORIZED, "the token has been disabled");
        }
        accessToken.setDisabled(true);
        accessTokenRepository.save(accessToken);
    }
}
