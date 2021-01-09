package com.elias.entity;

import com.elias.common.Constants;
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

    /**
     * 显示名称
     */
    @Column(name = "`display_name`", columnDefinition = "varchar(50) not null comment '显示名称'")
    private String displayName = Constants.DEFAULT_DISPLAY_NAME;

    /**
     * 头像地址
     */
    @Column(name = "`avatar`", columnDefinition = "varchar(100) not null comment '头像'")
    private String avatar = Constants.DEFAULT_AVATAR;

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
