package com.elias.common.cache;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import redis.clients.jedis.JedisCommands;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/12/3 4:07 下午</p>
 * <p>description: </p>
 */
public class RedisCacheOperator {
    /**
     * 随机过期时间毫秒值
     */
    private final static long RANDOM_EXPIRE_MILLIS = 60000;

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
     * 旧版本获取分布式锁
     * setnx key value -- key不存在时才设置
     * setex key seconds value -- 为key设置值的同时以秒为单位设置生存时间
     * setpx
     *
     * @param lockName 锁名称，即key
     * @param timeout  锁的失效时间，单位为毫秒
     * @return 是否获取成功
     */
    public boolean tryAcquireLock(String lockName, long timeout) {
        Boolean acquire = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            JedisCommands jedisCommands = (JedisCommands) connection.getNativeConnection();
            String result = jedisCommands.set(lockName, "", "NX", "PX", timeout);
            return "OK".equals(result);
        });
        return acquire != null && acquire;
    }

    /**
     * 将数据放入redis中指定的哈希表中，同时指定过期时间
     *
     * @param key       键
     * @param hashKey   哈希键
     * @param hashValue 值
     * @param timeout   有效时间
     * @param timeUnit  时间的单位。如果为null，就不设置过期时间
     */
    public void putAndExpire(String key, Object hashKey, Object hashValue, Long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
        if (timeUnit != null) {
            redisTemplate.expire(key, timeout, timeUnit);
        }
    }

    /**
     * 如果redis中不存在该key，就将数据放入redis，并同时在指定的过期时间上随机加上
     * [0, {@code RedisCacheOperator.RANDOM_EXPIRE_MILLIS})秒，该操作是原子性的
     *
     * @param key      key
     * @param value    value
     * @param timeout  过期时间
     * @param timeUnit 时间的单位
     */
    public void atomicSetIfAbsentAndExpireRandom(String key, Object value, long timeout, TimeUnit timeUnit) {
        if (value != null) {
            long millis = timeUnit.toMillis(timeout);
            millis += Math.random() * RANDOM_EXPIRE_MILLIS;
            redisTemplate.opsForValue().setIfAbsent(key, value, millis, TimeUnit.MILLISECONDS);
        }
    }
}
