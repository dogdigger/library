package com.elias.common.annotation;

import java.lang.annotation.*;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 4:43 下午</p>
 * <p>description: 定义一个注解，用于controller方法中，
 * 表明调用该controller方法必须要携带指定名称并符合指定模式的请求头</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequiredHeader {
    String name();
    String pattern();
}
