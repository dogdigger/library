package com.elias.reader.config;

import com.elias.common.aop.LogAspect;
import com.elias.common.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;

/**
 * @author chengrui
 * <p>create at: 2020-07-31 14:40</p>
 * <p>description: </p>
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(){
        return new GlobalExceptionHandler();
    }

    @Bean
    public LogAspect logAspect(){
        return new LogAspect();
    }
}
