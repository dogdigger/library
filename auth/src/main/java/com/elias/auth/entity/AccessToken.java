package com.elias.auth.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author chengrui
 * <p>create at: 2020/9/1 7:50 下午</p>
 * <p>description: 访问令牌实体类</p>
 */
@Entity
@Table(name = "t_access_token")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "expire_time", nullable = false, columnDefinition = "mediumint comment ''")
    private Integer expireTime;
}
