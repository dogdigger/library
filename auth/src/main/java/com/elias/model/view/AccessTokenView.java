package com.elias.model.view;

import com.elias.entity.AccessToken;
import com.elias.service.AccessTokenService;
import lombok.Data;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/19 3:54 下午</p>
 * <p>description: </p>
 */
@Data
public class AccessTokenView {
    /**
     * 令牌
     */
    private UUID id;

    /**
     * 令牌剩余的生存时间，单位为毫秒
     */
    private Integer ttl;

    public static AccessTokenView createFromAccessToken(AccessToken accessToken) {
        AccessTokenView view = new AccessTokenView();
        view.setId(accessToken.getId());
        view.setTtl(AccessTokenService.ttl(accessToken));
        return view;
    }
}
