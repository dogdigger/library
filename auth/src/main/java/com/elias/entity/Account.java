package com.elias.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/24 3:58 下午</p>
 * <p>description: 账号</p>
 */
@Data
@Entity
@Table(name = "t_account_info", indexes = {
        @Index(name = "user_unique_idx", columnList = "user_id", unique = true)
})
public class Account {
    /**
     * id，主键
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * 对应的用户id
     */
    @Column(name = "`user_id`", columnDefinition = "binary(16) not null comment '用户id'")
    private UUID userId;

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
     * 账号的启用状态
     */
    @Column(name = "`enabled`", columnDefinition = "bit(1) not null default true comment '是否启用'")
    private Boolean enabled = true;

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
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;
}
