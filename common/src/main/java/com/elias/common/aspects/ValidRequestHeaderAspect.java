package com.elias.common.aspects;

import com.elias.common.annotation.ValidRequestHeader;
import com.elias.common.annotation.Validator;
import com.elias.common.holder.SpringContextHolder;
import com.elias.common.exception.RequestHeaderNotFoundException;
import com.elias.common.holder.HttpServletRequestHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengrui
 * <p>create at: 2020/11/27 3:00 下午</p>
 * <p>description: </p>
 */
@Aspect
public class ValidRequestHeaderAspect {
    // 只拦截被 @ValidRequestHeader 修饰的方法
    public final String CONTROLLER_POINTCUT = "@annotation(com.elias.common.annotation.ValidRequestHeader)";

    @Around(CONTROLLER_POINTCUT)
    public Object doAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Class clazz = proceedingJoinPoint.getSignature().getDeclaringType();
        // 拦截被 @RestController或@Controller 修饰的类
        if (clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Controller.class)) {
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            ValidRequestHeader annotation = methodSignature.getMethod().getAnnotation(ValidRequestHeader.class);
            HttpServletRequest request;
            if ((request = HttpServletRequestHolder.getHttpServletRequest()) != null) {
                String headerValue = request.getHeader(annotation.headerName());
                if (headerValue == null) {
                    throw new RequestHeaderNotFoundException(annotation.headerName());
                }
                // 从上下文中获取 Validator 对象
                Validator validator = SpringContextHolder.getBean(annotation.validator());
                // 调用 Validator 对象的方法来校验请求头
                validator.validate(headerValue);
            }
        }
        return proceedingJoinPoint.proceed();
    }
}
