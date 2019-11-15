package me.linbo.web.common.lock.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.lock.IDistributedLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.zookeeper.ZookeeperProperties;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author LinBo
 * @date 2019-11-12 10:01
 */
@Slf4j
public abstract class ZkDistributedLock implements IDistributedLock {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    private InterProcessMutex lock;

    abstract String getLockPath();

    @PostConstruct
    public void init() {
        Objects.requireNonNull(zookeeperProperties);
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
                zookeeperProperties.getMaxRetries(),
                zookeeperProperties.getMaxSleepMs());
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getConnectString())
                .namespace("concurrent-locks")
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        String lockPath = getLockPath();
        this.lock = new InterProcessMutex(client, lockPath);
    }

    @Override
    public void lock() {
        try {
            lock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            lock();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean tryLock(long timeout) {
        try {
            return !lock.acquire(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void unlock() {
        try {
            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
