package com.elias.common.annotation;

import java.lang.annotation.*;

/**
 * @author chengrui
 * <p>create at: 2020/11/27 2:49 下午</p>
 * <p>description: 请求头认证</p>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRequestHeader {
    String headerName();
    Class<? extends Validator> validator();
}
