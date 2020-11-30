package com.elias.common.aspects;

import com.elias.common.holder.HttpServletRequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

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
    public Object logIt(ProceedingJoinPoint joinPoint) throws Throwable {
        Class declaringType = joinPoint.getSignature().getDeclaringType();
        if (declaringType.isAnnotationPresent(RestController.class) || declaringType.isAnnotationPresent(Controller.class)) {
            HttpServletRequest request;
            if ((request = HttpServletRequestHolder.getHttpServletRequest()) != null) {
                StringBuilder builder = new StringBuilder(512);
                builder.append("[Method=").append(request.getMethod()).
                        append(", User-Agent=").append(request.getHeader("User-Agent")).
                        append(", Remote-Address=").append(request.getRemoteAddr()).
                        append(", Remote-Host=").append(request.getRemoteHost()).
                        append(", Request-URL=").append(request.getRequestURL());
                // 非get请求就取出请求体
                if (!request.getMethod().equalsIgnoreCase("get")) {
                    builder.append(", body={");
                    Arrays.stream(joinPoint.getArgs()).forEach(builder::append);
                    builder.append("}");
                } else {
                    builder.append(", QueryString=").append(request.getQueryString());
                }
                log.info(builder.append("] was called").toString());
            }
        }
        return joinPoint.proceed();
    }

    @Around(TIME_IT_POINTCUT)
    public Object timeIt(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object res = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("{} cost {}ms to execute......", getTargetMethodName(joinPoint), end - start);
        return res;
    }

    private String getTargetMethodName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
    }
}
