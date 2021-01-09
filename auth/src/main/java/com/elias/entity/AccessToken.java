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
 * <p>create at: 2020/9/1 7:50 下午</p>
 * <p>description: 访问令牌实体类</p>
 */
@Entity
@Table(
        name = "t_access_token",
        indexes = {
              @Index(name = "idx_owner_id_type", columnList = "owner_id, owner_type", unique = true)
        }
)
@Data
public class AccessToken {

    /**
     * 令牌
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16) comment '主键，也是令牌'")
    private UUID id;

    /**
     * 令牌拥有者的id
     */
    @Column(name = "`owner_id`", columnDefinition = "binary(16) not null comment '令牌的拥有者，可能是clientUserId，也可能是clientId'")
    private UUID ownerId;

    /**
     * 令牌的类型。1表示客户端令牌，2表示用户令牌
     */
    @Column(name = "`owner_type`", columnDefinition = "tinyint unsigned not null comment '令牌拥有者的类型，1表示客户端令牌，2表示用户令牌'")
    private Integer ownerType;

    /**
     * 令牌的有效时间---单位为毫秒
     */
    @Column(name = "`expire`", columnDefinition = "int unsigned not null comment '令牌的有效时间，单位: 毫秒'")
    private Integer expire;

    /**
     * 令牌的创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "create_time", updatable = false, columnDefinition = "datetime comment '令牌的创建时间'")
    private Date createTime;

    /**
     * 令牌的最后更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "update_time", columnDefinition = "datetime comment '令牌的最后更新时间'")
    private Date updateTime;

    public enum OwnerType {
        /**
         * 客户端
         */
        CLIENT(1),

        /**
         * 用户
         */
        USER(2);

        /**
         * 标识拥有者的类型
         */
        private final int type;

        OwnerType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }

}
