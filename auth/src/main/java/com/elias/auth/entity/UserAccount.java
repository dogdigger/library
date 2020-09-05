package com.elias.auth.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:11 下午</p>
 * <p>description: </p>
 */
@Entity
@Table(name = "t_user_account")
@Data
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "account", nullable = false, updatable = false, unique = true, columnDefinition = "varchar(30) comment '用户的账号'")
    private String account;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(50) comment '用户的密码'")
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "bit(1) default 1 comment '是否启用'")
    private boolean enabled;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "error_count", nullable = false, columnDefinition = "tinyint unsigned default 0 comment '密码输错的次数'")
    private Integer errorCount;

    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
