package com.elias.service;

import com.elias.entity.AccessToken;
import com.elias.model.view.AccessTokenView;
import com.elias.repository.AccessTokenRepository;
import com.elias.repository.ClientRepository;
import com.elias.repository.UserRepository;
import com.elias.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:06 下午</p>
 * <p>description: </p>
 */
@Service
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final Integer DEFAULT_USER_TOKEN_EXPIRE = 7200;//2小时
    private final Integer DEFAULT_CLIENT_TOKEN_EXPIRE = 604800;//7天

    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    /**
     * 获取令牌。重复获取不会生成新令牌
     *
     * @param ownerId   令牌的接受者id，可能是一个client，也可能是一个user
     * @param ownerType 接受者的类型
     * @return {@link AccessTokenView}
     */
    public AccessTokenView getToken(UUID ownerId, AccessToken.OwnerType ownerType) {
        AccessToken accessToken = accessTokenRepository.findByOwnerIdAndOwnerType(ownerId, ownerType.getType());
        if (accessToken != null && isTokenExpired(accessToken)) {
            // 如果令牌已过期就删除
            accessTokenRepository.delete(accessToken);
            accessToken = null;
        }
        int expire = ownerType.getType() == AccessToken.OwnerType.CLIENT.getType() ?
                DEFAULT_CLIENT_TOKEN_EXPIRE : DEFAULT_USER_TOKEN_EXPIRE;
        if (accessToken == null) {
            accessToken = new AccessToken();
            accessToken.setOwnerId(ownerId);
            accessToken.setOwnerType(ownerType.getType());
            accessToken.setExpire(expire);
            accessToken = accessTokenRepository.save(accessToken);
        }
        AccessTokenView view = new AccessTokenView();
        BeanUtils.copyProperties(accessToken, view);
        view.setExpire((int) (DateUtils.timestamp(accessToken.getCreatedDate()) + expire - DateUtils.timestampNow()));
        return view;
    }

    /**
     * 令牌是否已过期
     *
     * @param accessToken 令牌实体对象，{@link AccessToken}
     * @return boolean
     */
    private boolean isTokenExpired(AccessToken accessToken) {
        return DateUtils.timestampNow() >= DateUtils.timestamp(accessToken.getCreatedDate()) + accessToken.getExpire();
    }
}
