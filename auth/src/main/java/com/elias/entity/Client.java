package com.elias.entity;

import com.elias.util.AppSecretUtils;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/21 12:50 下午</p>
 * <p>description: 申请人提供相应的信息，然后由系统管理员来创建</p>
 */
@Entity
@Data
@Table(name = "t_client_info", indexes = {
        @Index(name = "idx_name", columnList = "`name`")
})
public class Client implements Serializable {

    /**
     * 客户端的id
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "`id`", columnDefinition = "binary(16) comment '主键，客户端的id'")
    private UUID id;

    /**
     * 客户端名称--由业务来保证唯一性
     */
    @Column(name = "`name`", nullable = false, columnDefinition = "varchar(50) comment '客户端名称'")
    private String name;

    /**
     * 客户端简介
     */
    @Column(name = "`description`", nullable = false, columnDefinition = "varchar(100) comment '客户端简介'")
    private String description;

    /**
     * 分配给客户端的密码。固定为22个字符，由{@link AppSecretUtils}负责生成，实质上是将一个随机UUID进行编码得到
     */
    @Column(name = "`secret`", nullable = false, columnDefinition = "char(22) comment '客户端的secret'")
    private String secret;

    /**
     * 记录的创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private Date createTime;
}
