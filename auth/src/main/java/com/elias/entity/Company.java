package com.elias.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/23 10:33 上午</p>
 * <p>description: 创建企业的步骤：
 *  (1) 申请一个appKey和appSecret
 *  (2) 通过appKey和appSecret来创建企业
 * </p>
 */
@Entity
@Data
@Table(name = "company", indexes = {
        // code和name列上都建立普通索引，业务在插入数据时需要保证这两个不能重复
        @Index(name = "idx_code", columnList = "code"),
        @Index(name = "idx_name", columnList = "name")
})
public class Company {
    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16) comment '主键，也是企业的id'")
    private UUID id;

    @Column(name = "code", nullable = false, updatable = false, columnDefinition = "char(10) comment '企业的id'")
    private String code;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50) comment '企业的名称'")
    private String name;

    @Column(name = "creator", nullable = false, updatable = false, columnDefinition = "binary(16) comment '是由哪个user创建的'")
    private UUID creator;

    @Column(name = "address", nullable = false, updatable = false, columnDefinition = "varchar(100) comment '企业地址'")
    private String address;

    @Column(name = "status", nullable = false, columnDefinition = "tinyint unsigned default 1 comment '企业状态：1:未审核, 2:审核不通过， 3:审核通过'")
    private Integer status;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
