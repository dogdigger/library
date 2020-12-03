package com.elias.config;

import com.elias.common.cache.RedisCacheOperator;
import com.elias.common.cache.RedisTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author chengrui
 * <p>create at: 2020/12/3 5:19 下午</p>
 * <p>description: </p>
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheOperator redisCacheOperator() {
        return new RedisCacheOperator(RedisTemplateFactory.genericJacksonSerializerTemplate(new LettuceConnectionFactory()));
    }
}
