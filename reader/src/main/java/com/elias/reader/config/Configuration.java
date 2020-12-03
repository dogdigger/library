package com.elias.reader.config;

import com.elias.common.aspects.LogAspect;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

/**
 * @author chengrui
 * <p>create at: 2020-07-31 14:40</p>
 * <p>description: </p>
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

    private final LettuceConnectionFactory lettuceConnectionFactory;

    public Configuration(LettuceConnectionFactory lettuceConnectionFactory) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
    }

    @Bean
    public LogAspect logAspect(){
        return new LogAspect();
    }

    @Bean("redisCacheManager")
    public CacheManager cacheManager() {
        RedisCacheConfiguration userCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().
                // 缓存失效时间
                entryTtl(Duration.ofDays(1)).
                // key序列化
                serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())).
                // value序列化
                serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonRedisSerializer()));

        return RedisCacheManager.builder(lettuceConnectionFactory).
                withCacheConfiguration("user", userCacheConfiguration).
                build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 连接工厂
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        // key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value序列化
        redisTemplate.setValueSerializer(jacksonRedisSerializer());
        // hash key序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // hssh value序列化
        redisTemplate.setHashValueSerializer(jacksonRedisSerializer());
        return redisTemplate;
    }

    public RedisSerializer<Object> jacksonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public RedisCacheOperator redisCacheOperator() {
        return new RedisCacheOperator(redisTemplate());
    }
}
