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
     * 非阻塞式的获取分布式锁。这个方法要求 spring-data-redis 的版本不低于2.1，因为
     * setIfAbsent(K key, V value, long timeout, TimeUnit unit) 是在2.1版本中新增的。早期版本中的setIfAbsent无法同时设置过期时间，
     * 若先使用setIfAbsent，再使用expire设置键的过期时间，会产生死锁的风险:
     * 如果setIfAbsent之后，线程挂掉了，那么这个锁就会一直存在，其他的线程就永远也拿不到锁了，所以必须要保证setIfAbsent和expire
     * 这两个操作的原子性。redisTemplate中并没有这样的方法，但是jedis中是有这样的方法。
     * <p>
     * 手动释放锁可以通过 delete(lockName) 来实现
     *
     * @param lockName 锁在redis中的key
     * @param timeout  锁的有效时间
     * @param timeUnit 时间的单位
     * @return 一个boolean值，表示是否获取成功
     */
    public boolean tryAcquireLock(String lockName, long timeout, TimeUnit timeUnit) {
        Boolean acquire = redisTemplate.opsForValue().setIfAbsent(lockName, "", timeout, timeUnit);
        return acquire != null && acquire;
    }

    /**
     * 阻塞式的获取分布式锁
     *
     * @param lockName 锁在redis中的key
     * @param timeout  锁的有效时间
     * @param timeUnit 时间的单位
     */
    public void acquireLock(String lockName, long timeout, TimeUnit timeUnit) {
        boolean acquired = false;
        while (!acquired) {
            acquired = tryAcquireLock(lockName, timeout, timeUnit);
        }
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
     * 如果redis中不存在该key，就将数据放入redis，并同时在指定的过期时间上随机加上[0, 30)秒
     *
     * @param key      key
     * @param value    value
     * @param timeout  过期时间
     * @param timeUnit 时间的单位
     */
    public void setIfAbsentAndExpireRandom(String key, Object value, long timeout, TimeUnit timeUnit) {
        if (value != null) {
            long millis = timeUnit.toMillis(timeout);
            millis += Math.random() * 30000;
            redisTemplate.opsForValue().setIfAbsent(key, value, millis, TimeUnit.MILLISECONDS);
        }
    }
}
