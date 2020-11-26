package com.elias.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:11 下午</p>
 * <p>description: 通过手机号码进行注册</p>
 */
@Entity
@Table(
        name = "t_user_info",
        indexes = {
            @Index(name = "idx_mobile_unique", unique = true, columnList = "mobile"),
            @Index(name = "idx_email", columnList = "email")
        }
)
@Data
public class User {
    /**
     * 用户id
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "`id`", columnDefinition = "binary(16) comment '主键，也是用户的id'")
    private UUID id;

    /**
     * 用户姓名-不为空
     */
    @Column(name = "`name`", columnDefinition = "varchar(50) not null comment '用户的姓名'")
    private String name;

    /**
     * 用户的性别-不为空，默认为1
     */
    @Column(name = "`gender`", columnDefinition = "tinyint unsigned not null default 1 comment '性别'")
    private Integer gender;

    /**
     * 头像地址
     */
    @Column(name = "`avatar`", columnDefinition = "varchar(100) comment '头像的地址'")
    private String avatar;

    /**
     * 手机号码。不能为空，唯一
     */
    @Column(name = "`mobile`", columnDefinition = "char(11) not null comment '手机号码'")
    private String mobile;

    /**
     * 邮箱。如果不空，必须唯一
     */
    @Column(name = "`email`", columnDefinition = "varchar(30) comment '邮箱'")
    private String email;

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
