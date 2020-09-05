package com.elias.auth.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/1 7:50 下午</p>
 * <p>description: 访问令牌实体类</p>
 */
@Entity
@Table(name = "t_access_token")
@Data
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "bigint unsigned comment '自增主键'")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true, columnDefinition = "int comment '用户的编号'")
    private Integer userId;

    @Column(name = "token", nullable = false, unique = true, columnDefinition = "char(36) comment '用户的令牌'")
    private UUID token;

    @Column(name = "disabled", nullable = false, columnDefinition = "bit(1) default 0 comment '令牌是否已被销毁'")
    private Boolean disabled;

    @Column(name = "create_time", nullable = false, columnDefinition = "datetime comment '令牌的创建时间'")
    private Date createTime;

    @Column(name = "survive_time", nullable = false, columnDefinition = "smallint unsigned comment '令牌的有效时间，单位: 秒'")
    private Integer surviveTime;
}
