package me.linbo.web.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @author LinBo
 * @date 2019-11-21 15:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedissonTest {

    private RedissonClient redisson;

    private String lockName = "test-lock:order-no";

    @Before
    public void doBefore() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379");
        redisson = Redisson.create(config);
    }

    @Test
    public void testLock() throws InterruptedException {
        RLock lock = redisson.getLock(lockName);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            log.info("线程 {} 开始获取锁", Thread.currentThread().getName());
            lock.lock(20000, TimeUnit.MILLISECONDS);
            log.info("线程 {} 获取锁成功", Thread.currentThread().getName());
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("线程 {} 开始解锁", Thread.currentThread().getName());
            lock.unlock();
            log.info("线程 {} 解锁成功", Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            log.info("线程 {} 开始获取锁", Thread.currentThread().getName());
            lock.lock(20000, TimeUnit.MILLISECONDS);
            log.info("线程 {} 获取锁成功", Thread.currentThread().getName());
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("线程 {} 开始解锁", Thread.currentThread().getName());
            lock.unlock();
            log.info("线程 {} 解锁成功", Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
    }

    @Test
    public void testTimeout() throws InterruptedException {
        RLock lock = redisson.getLock(lockName);
        log.info("开始上锁");
        lock.lock(3000L, TimeUnit.MILLISECONDS);
        log.info("上锁成功");
        Thread.sleep(4000L);
        log.info("尝试解锁");
        lock.unlock();
    }

    @Test
    public void testRAtomicLongPerformance() throws ExecutionException, InterruptedException {
        int seconds = 10;
        int processors = Runtime.getRuntime().availableProcessors();
        RAtomicLong atomicLong = redisson.getAtomicLong("test:sequence:RAtomicLong-no");
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        ArrayList<Future<Long>> task = new ArrayList<>(processors);
        int count = processors;
        while (count-- > 0) {
            long endTime = System.currentTimeMillis() + seconds * 1000;
            task.add(executor.submit(() -> {
                long total = 0;
                while (endTime > System.currentTimeMillis()) {
                    atomicLong.incrementAndGet();
                    total++;
                }
                log.info("持续{}秒获取到{}个序列值", seconds, total);
                return total;
            }));
        }
        long sum = 0;
        for (Future<Long> future : task) {
            sum += future.get();
        }
        log.info("{}线程，花费{}秒，获取序列值{}个，平均一个线程一秒{}获取个序列值", processors, seconds, sum, (sum/seconds/processors));
    }

}
