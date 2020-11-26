package com.elias.common.aop;

import com.elias.common.annotation.RequiredHeader;
import com.elias.common.exception.CommonModuleException;
import com.elias.common.exception.ErrorCode;
import com.elias.common.exception.RestException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 4:53 下午</p>
 * <p>description: 拦截com.elias包下的api子包下被RequiredHeader注解修饰的方法</p>
 */
@Aspect
@Slf4j
@Component
public class RequiredHeaderAspect {
    // 拦截api接口
    private static final String CONTROLLER_POINTCUT = "execution(* com.elias.*.api..*.*(..))";

    @Around(CONTROLLER_POINTCUT)
    public Object doAspect(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        if(methodSignature.getMethod().isAnnotationPresent(RequiredHeader.class)) {
            RequiredHeader annotation = methodSignature.getMethod().getAnnotation(RequiredHeader.class);
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
                String headerValue = httpServletRequest.getHeader(annotation.name());
                if (headerValue == null) {
                    throw new RestException("request header `" + annotation.name() + "` is required");
                }
                if (!headerValue.matches(annotation.pattern())) {
                    throw new CommonModuleException("the value of request header `" + annotation.name() + "` is illegal");
                }
            }
        }
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("");
            if (throwable instanceof RestException) {
                throw (RestException)throwable;
            }
            throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
