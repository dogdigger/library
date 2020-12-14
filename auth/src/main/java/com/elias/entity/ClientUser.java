package com.elias.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/12/14 5:18 下午</p>
 * <p>description: 客户端的用户: 同一个user，在不同的客户端状态、密码等信息可能不同</p>
 */
@Data
@Entity
@Table(name = "t_client_user", indexes = {
        @Index(name = "idx_client_user", columnList = "client_id, user_id", unique = true)
})
public class ClientUser {
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * 客户端id
     */
    @Column(name = "`client_id`", columnDefinition = "binary(16) not null comment '用于标识不同的客户端'")
    private UUID clientId;

    /**
     * 用户ID
     */
    @Column(name = "`user_id`", columnDefinition = "binary(16) not null comment '对应于user表的ID'")
    private UUID userId;

    @Column(name = "`status`", columnDefinition = "bit(1) default true not null comment '账号的启用状态'")
    private Boolean enabled = true;

    /**
     * 显示名称
     */
    @Column(name = "`display_name`", columnDefinition = "varchar(50) not null comment '在不同客户端的名称'")
    private String displayName;

    /**
     * 密码---用户名是手机号码或者邮箱
     */
    @Column(name = "`password`", columnDefinition = "varchar(255) not null comment '加密后的密码'")
    private String password;

    /**
     * 密码的盐，固定长度为16。随机生成
     */
    @Column(name = "`salt`", columnDefinition = "char(16) not null comment '密码的盐'")
    private String salt;

    /**
     * 记录的创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;

    /**
     * 记录的最后更新时间
     */
    @Column(name = "`update_time`", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;
}
