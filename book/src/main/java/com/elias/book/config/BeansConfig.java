package com.elias.book.config;

import com.elias.common.aspects.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chengrui
 * <p>create at: 2020-07-28 19:23</p>
 * <p>description: </p>
 */
@Configuration
public class BeansConfig {

    @Bean
    public LogAspect logAspect(){
        return new LogAspect();
    }
}
