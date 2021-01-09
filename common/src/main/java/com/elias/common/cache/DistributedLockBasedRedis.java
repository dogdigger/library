package com.elias.common.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/12/31 4:34 下午</p>
 * <p>description: 基于redis的分布式锁实现</p>
 */
public class DistributedLockBasedRedis {
    private final RedisCacheOperator redisCacheOperator;
    private final String lockName;
    private final long timeout;
    private final TimeUnit timeUnit;
    private volatile Thread exclusiveOwnerThread;

    public DistributedLockBasedRedis(String lockName, long timeout, TimeUnit timeUnit) {
        if (StringUtils.isEmpty(lockName)) {
            throw new IllegalArgumentException("分布式锁的名称不允许为空");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("分布式锁的过期时间必须大于0");
        }
        this.lockName = lockName;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.redisCacheOperator = new RedisCacheOperator(new RedisTemplate<>());
    }

    /**
     * 非阻塞式的获取分布式锁(可重入的获取)。这个方法要求 spring-data-redis 的版本不低于2.1，因为
     * setIfAbsent(K key, V value, long timeout, TimeUnit unit) 是在2.1版本中新增的。早期版本中的setIfAbsent无法同时设置过期时间，
     * 若先使用setIfAbsent，再使用expire设置键的过期时间，会产生死锁的风险:
     * 如果setIfAbsent之后，线程挂掉了，那么这个锁就会一直存在，其他的线程就永远也拿不到锁了，所以必须要保证setIfAbsent和expire
     * 这两个操作的原子性。redisTemplate中并没有这样的方法，但是jedis中是有这样的方法。
     * <p>
     * 手动释放锁可以通过 delete(lockName) 来实现
     *
     * @return 一个boolean值，表示是否获取成功
     */
    public boolean tryAcquire() {
        Boolean acquire = redisCacheOperator.valueOperations().setIfAbsent(lockName, "", timeout, timeUnit);
        if (acquire != null) {
            return acquire || exclusiveOwnerThread == Thread.currentThread();
        }
        return false;
    }

    /**
     * 阻塞式的获取分布式锁。
     */
    public void acquire() throws InterruptedException {
        boolean acquired = tryAcquire();
        while (!acquired) {
            // 如果没有获取到锁，则等待10毫秒之后再次尝试
            lockName.wait(10);
            acquired = tryAcquire();
        }
    }

    /**
     * 超时阻塞式的获取分布式锁
     *
     * @param timeout  超时时间
     * @param timeUnit 超时时间的单位
     * @return boolean
     * @throws InterruptedException InterruptedException
     */
    public boolean acquire(long timeout, TimeUnit timeUnit) throws InterruptedException {
        long now = System.currentTimeMillis();
        while (System.currentTimeMillis() - now < timeUnit.toMillis(timeout)) {
            if (tryAcquire()) {
                return true;
            }
            lockName.wait(10);
        }
        return false;
    }

    /**
     * 释放锁
     */
    public void release() {
        if (exclusiveOwnerThread != Thread.currentThread()) {
            throw new IllegalStateException("当前线程不是分布式锁的持有线程，无法释放锁");
        }
        redisCacheOperator.delete(lockName);
        exclusiveOwnerThread = null;
        // 唤醒等到获取锁的所有线程
        lockName.notifyAll();
    }

    public String getLockName() {
        return this.lockName;
    }

}
