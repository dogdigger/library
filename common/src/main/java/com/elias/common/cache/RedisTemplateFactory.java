package com.elias.common.cache;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author chengrui
 * <p>create at: 2020/12/3 5:25 下午</p>
 * <p>description: </p>
 */
public final class RedisTemplateFactory {
    private RedisTemplateFactory(){}

    public static <T> RedisTemplate<String, T> genericJacksonSerializerTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        // 连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);
        // key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash key序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // hash value序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
