package com.elias.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

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
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16) comment '主键，也是令牌'")
    private UUID id;

    @Column(name = "owner_id", columnDefinition = "binary(16) comment '令牌的拥有者，可能是userId，也可能是clientId'")
    private UUID ownerId;

    @Column(name = "is_client_token", columnDefinition = "bit(1) comment '是否是客户端令牌'")
    private Boolean isClientToken;

    @Column(name = "created_date", nullable = false, columnDefinition = "datetime comment '令牌的创建时间'")
    private Date createdDate;

    @Column(name = "survival_time", nullable = false, columnDefinition = "smallint unsigned comment '令牌的有效时间，单位: 毫秒'")
    private Integer survivalTime;
}
