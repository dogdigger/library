package com.elias.auth.view;

import lombok.Data;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:10 下午</p>
 * <p>description: </p>
 */
@Data
public class AccessTokenView {
    private UUID token;
    private Integer surviveTime;
}
