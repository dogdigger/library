package com.elias.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:11 下午</p>
 * <p>description: 通过手机号码进行注册</p>
 */
@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16) comment '主键，也是用户的id'")
    private UUID id;

    @Column(name = "mobile", nullable = false, updatable = false, unique = true, columnDefinition = "char(11) comment '手机号码'")
    private String mobile;

    @Column(name = "account", nullable = false, updatable = false, unique = true, columnDefinition = "varchar(20) comment '用户账号'")
    private String account;

    @Column(name = "pwd_salt", nullable = false, columnDefinition = "varchar(75) comment '密码'")
    private String pwdSalt;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
