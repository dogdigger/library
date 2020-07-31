package com.elias.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 19:23</p>
 * <p>description: 日志切面，用于记录日志</p>
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    private static final String LOG_IT_POINTCUT = "@annotation(com.elias.common.annotation.LogIt)";
    private static final String TIME_IT_POINTCUT = "@annotation(com.elias.common.annotation.TimeIt)";

    @Around(LOG_IT_POINTCUT)
    public Object logIt(ProceedingJoinPoint joinPoint){
        Class declaringType = joinPoint.getSignature().getDeclaringType();
        if(declaringType.isAnnotationPresent(RestController.class) || declaringType.isAnnotationPresent(Controller.class)){
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if(requestAttributes != null){
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)requestAttributes;
                HttpServletRequest request = servletRequestAttributes.getRequest();
                StringBuilder builder = new StringBuilder();
                builder.append("[Method=").append(request.getMethod()).
                        append(", User-Agent=").append(request.getHeader("User-Agent")).
                        append(", Remote-Address=").append(request.getRemoteAddr()).
                        append(", Remote-Host=").append(request.getRemoteHost()).
                        append(", Request-URL=").append(request.getRequestURL());
                // 非get请求就取出请求体
                if(!request.getMethod().equalsIgnoreCase("get")){
                    builder.append(", body={");
                    for (Object arg : joinPoint.getArgs()){
                        builder.append(arg);
                    }
                    builder.append("}");
                }else {
                    builder.append(", QueryString=").append(request.getQueryString());
                }
                log.info(builder.append("]").toString());
            }
        }
        try{
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("serious error: " + throwable.getMessage());
            return null;
        }
    }

    @Around(TIME_IT_POINTCUT)
    public Object timeIt(ProceedingJoinPoint joinPoint){
        long start = System.currentTimeMillis();
        try{
            Object res = joinPoint.proceed();
            long end = System.currentTimeMillis();
            log.info("{} cost {}ms to execute......", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), end-start);
            return res;
        } catch (Throwable throwable) {
            log.error("serious error: " + throwable.getMessage());
            return null;
        }
    }
}
