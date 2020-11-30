package com.elias.common.aspects;

import com.elias.common.annotation.RequireRequestHeader;
import com.elias.common.exception.BadRequestHeaderException;
import com.elias.common.exception.RequestHeaderNotFoundException;
import com.elias.common.holder.HttpServletRequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 4:53 下午</p>
 * <p>description: 拦截com.elias包下的api子包下被RequiredHeader注解修饰的方法</p>
 */
@Aspect
@Slf4j
@Component
public class RequireRequestHeaderAspect {
    // 拦截被 @RequireRequestHeader 注解修饰的方法
    private static final String CONTROLLER_POINTCUT = "@annotation(com.elias.common.annotation.RequireRequestHeader)";

    @Around(CONTROLLER_POINTCUT)
    public Object doAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Class clazz = proceedingJoinPoint.getSignature().getDeclaringType();
        // 只拦截被 @RestController或@Controller 修饰的类
        if (clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Controller.class)) {
            Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
            RequireRequestHeader annotation = method.getAnnotation(RequireRequestHeader.class);
            HttpServletRequest request;
            // 拦截被 @RequiredHeader 修饰的方法
            if ((request = HttpServletRequestHolder.getHttpServletRequest()) != null) {
                String headerValue = request.getHeader(annotation.name());
                if (headerValue == null) {
                    throw new RequestHeaderNotFoundException(annotation.name());
                }
                if (!headerValue.matches(annotation.pattern())) {
                    throw new BadRequestHeaderException("the value of request header `" + annotation.name() + "` does not match " + annotation.pattern());
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }
}
