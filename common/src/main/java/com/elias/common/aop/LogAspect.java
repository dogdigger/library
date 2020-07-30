package com.elias.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 19:23</p>
 * <p>description: 日志切面，用于记录日志</p>
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    private static final String pointCut = "execution(* com.elias..*.*(..)) && @annotation(com.elias.common.annotation.LogIt)";

    {
        System.out.println("创建LogAspect实例");
    }

    @Around(pointCut)
    public Object logIt(ProceedingJoinPoint joinPoint){
        String  className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info(className + "." + methodName + " was called");
        try{
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("系统发生错误: " + throwable.getMessage());
            return null;
        }
    }
}
