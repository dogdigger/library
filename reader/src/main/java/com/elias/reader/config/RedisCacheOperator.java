package com.elias.reader.config;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/12/3 4:07 下午</p>
 * <p>description: </p>
 */
public class RedisCacheOperator {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheOperator(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public ValueOperations<String, Object> valueOperations() {
        return redisTemplate.opsForValue();
    }

    public HashOperations<String, Object, Object> hashOperations() {
        return redisTemplate.opsForHash();
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }
}
