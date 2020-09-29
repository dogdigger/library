package com.elias.entity;

import com.elias.util.AppSecretUtils;
import com.elias.util.MessageDigestUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/21 12:50 下午</p>
 * <p>description: 申请人提供相应的信息，然后由系统管理员来创建</p>
 */
@Entity
@Table(name = "client", indexes = {
        @Index(name = "idx_appKey_appSecret", columnList = "app_key,app_secret", unique = true)
})
@Data
public class Client {

    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16) comment '主键'")
    private UUID id;

    /**
     * 申请人的手机号码
     */
    @Column(name = "mobile", nullable = false, updatable = false, columnDefinition = "char(11) comment '申请人的手机号码'")
    private String mobile;

    /**
     * 客户端名称
     */
    @Column(name = "name", nullable = false, columnDefinition = "varchar(50) comment '客户端名称'")
    private String name;

    /**
     * 客户端简介
     */
    @Column(name = "description", nullable = false, columnDefinition = "varchar(100) comment '客户端简介'")
    private String description;

    /**
     * 分配给客户端的appKey。由clientName、description、申请时间、随机数通过SHA-1哈希得来。
     * 由{@link MessageDigestUtils}负责哈希
     */
    @Column(name = "app_key", nullable = false, columnDefinition = "varchar(36) comment '分配给客户端的appKey'")
    private String appKey;

    /**
     * 分配给客户端的密码。固定为22个字符，由{@link AppSecretUtils}负责生成，实质上是将一个随机UUID进行编码得到
     */
    @Column(name = "app_secret", nullable = false, columnDefinition = "char(22) comment '客户端的appSecret'")
    private String appSecret;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "create_time", nullable = false)
    private Date createTime;
}
