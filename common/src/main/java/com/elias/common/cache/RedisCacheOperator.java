package com.elias.common.cache;

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

    /**
     * 获取分布式锁。此方式存在缺陷，如果setIfAbsent之后，线程挂掉了，那么这个锁就会一直存在，其他的线程就永远也拿不到锁了，
     * 所以必须要保证setIfAbsent和expire这两个操作的原子性。redisTemplate中并没有这样的方法，但是jedis中是有这样的方法。
     *
     * @param lockName
     * @param lockValue
     * @param timeout
     * @param timeUnit
     * @param <T>
     * @return
     */
    public <T> boolean getDistributedLock(String lockName, T lockValue, long timeout, TimeUnit timeUnit) {
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockName, lockValue);
        if (locked != null && locked) {
            redisTemplate.expire(lockName, timeout, timeUnit);
            return true;
        }
        return false;
    }
}
