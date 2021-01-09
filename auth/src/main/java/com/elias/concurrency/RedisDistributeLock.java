package com.elias.concurrency;

import com.elias.common.Constants;
import com.elias.common.cache.DistributedLockBasedRedis;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2021/1/5 7:48 下午</p>
 * <p>description: </p>
 */
@Slf4j
public class RedisDistributeLock extends DistributedLockBasedRedis {

    public RedisDistributeLock(String lockName, long timeout, TimeUnit timeUnit) {
        super(lockName, timeout, timeUnit);
    }

    public RedisDistributeLock(String lockName) {
        super(lockName, Constants.DISTRIBUTED_LOCK_EXPIRE, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean acquire(long timeout, TimeUnit timeUnit) {
        try {
            return super.acquire(timeout, timeUnit);
        } catch (InterruptedException e) {
            log.warn("获取分布式锁 {} 失败", super.getLockName());
            throw new RestException(ErrorCode.CONCURRENCY_ERROR);
        }
    }

    @Override
    public void acquire() {
        try {
            super.acquire();
        }catch (InterruptedException e) {
            log.warn("获取分布式锁 {} 失败", super.getLockName());
            throw new RestException(ErrorCode.CONCURRENCY_ERROR);
        }
    }
}
