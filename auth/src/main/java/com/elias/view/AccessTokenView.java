package com.elias.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:10 下午</p>
 * <p>description: </p>
 */
@Data
public class AccessTokenView {
    /**
     * 令牌
     */
    private UUID token;

    /**
     * 令牌的有效时间，单位为毫秒
     */
    private Integer survivalTime;

    /**
     * 令牌的生成时间
     */
    private Date createdDate;

    @JsonIgnore
    private UUID ownerId;
}
