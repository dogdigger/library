package com.elias.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/29 5:54 下午</p>
 * <p>description: </p>
 */
@Entity
@Data
@Table(name = "company", indexes = {
        // code和name列上都建立普通索引，业务在插入数据时需要保证这两个不能重复
        @Index(name = "idx_code", columnList = "code"),
        @Index(name = "idx_name", columnList = "name")
})
public class Staff {
    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16) comment '主键，也是企业的id'")
    private UUID id;
}
