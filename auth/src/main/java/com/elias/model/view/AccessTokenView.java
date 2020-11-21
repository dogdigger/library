package com.elias.model.view;

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
     * 令牌的有效时间，单位为秒
     */
    private Integer expire;
}
