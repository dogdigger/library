package com.elias.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 10:50 上午</p>
 * <p>description: 手机验证码</p>
 */
@Data
@Table(name = "t_verify_code", indexes = {
        @Index(name = "mobile_code_unique_idx", columnList = "mobile, code", unique = true)
})
@Entity
public class VerifyCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 手机号码
     */
    @Column(name = "`mobile`", columnDefinition = "char(11) not null comment '手机号码'")
    private String mobile;

    /**
     * 验证码，固定为6位数字
     */
    @Column(name = "`code`", columnDefinition = "char(6) not null comment '验证码'")
    private String code;

//    /**
//     * 该验证码是否已被使用
//     */
//    @Column(name = "`used`", columnDefinition = "bit(1) default false not null comment '是否已使用'")
//    private Boolean used = false;

    /**
     * 验证码的有效时间，单位是分钟，一般是10分钟
     */
    @Column(name = "`expire`", columnDefinition = "tinyint unsigned not null comment '有效时间，单位为分钟'")
    private Integer expire;

    /**
     * 记录的创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;
}
