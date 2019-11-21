package me.linbo.web.common.lock.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.lock.DistributedLockException;
import me.linbo.web.common.lock.IDistributedLock;
import me.linbo.web.common.spring.SpringContextHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 基于redis的分布式锁实现
 * @author LinBo
 * @date 2019-11-21 10:46
 */
@Slf4j
public class RedisDistributedLock implements IDistributedLock {

    private RedissonClient redissonClient;
    private RLock lock;

    /** redis锁对应的key */
    private String lockKey;

    public RedisDistributedLock(String lockName) {
        this.lockKey = "concurrent:lock:" + lockName;
        redissonClient = SpringContextHolder.getBean(RedissonClient.class);
        lock = redissonClient.getLock(this.lockKey);
        log.info("初始化redis分布式锁： {}", this.lockKey);
    }

    @Override
    public void lock() throws DistributedLockException {
        lock(DEFAULT_TIMEOUT);
    }

    @Override
    public boolean tryLock() {
        try {
            lock(DEFAULT_TIMEOUT);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis分布式锁上锁失败", e);
            return false;
        }
    }

    @Override
    public boolean tryLock(long timeout) {
        try {
            lock(timeout);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis分布式锁上锁失败", e);
            return false;
        }
    }

    private void lock(long timeout) throws DistributedLockException {
        lock.lock(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void unlock() throws DistributedLockException {
        try {
            lock.unlock();
        } catch (Exception e) {
            log.error("释放redis分布式锁失败", e);
            throw new DistributedLockException(e);
        }
    }
}
