package com.elias.common.annotation;

import java.lang.annotation.*;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 19:37</p>
 * <p>description:
 *  定义一个打印日志的注解，只能用于方法级别。使用方式：要将LogAspect这个类注入到项目中，然后在要打印日志的方法上加上这个注解即可。
 *  请注意：该注解只对controller方法起作用
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogIt {
}
