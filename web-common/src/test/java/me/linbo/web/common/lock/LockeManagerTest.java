package me.linbo.web.common.lock;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.lock.impl.ZkDistributedLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LockeManagerTest {

    @Test
    public void testZkLock() {
        IDistributedLock lock = LockeManager.ZK_ORDER_NO;
        // zk预热
        lock.lock();
        lock.unlock();
        testLock(lock);
    }


    @Test
    public void testRedisLock() {
        IDistributedLock lock = LockeManager.REDIS_ORDER_NO;
        testLock(lock);
    }


    @Test
    public void testZkLockPerformance() {
        // zk预热
        LockeManager.ZK_ORDER_NO.lock();
        LockeManager.ZK_ORDER_NO.unlock();
        testLockPerformance(LockeManager.ZK_ORDER_NO);
    }



    private void testLock(IDistributedLock lock) {
        int count = 10;
        ExecutorService executor = Executors.newFixedThreadPool(count);
        ArrayList<Future> task = new ArrayList<>(count);
        while (count-- > 0) {
            int finalCount = count;
            task.add(executor.submit(() -> {
                boolean isLock = false;
                try {
                    log.info(finalCount + "开始上锁:" + Thread.currentThread().getName());
                    lock.lock();
                    log.info(finalCount + "上锁成功:" + Thread.currentThread().getName());
                    isLock = true;
                    try {
                        Thread.sleep(IDistributedLock.DEFAULT_TIMEOUT - 200);
                    } catch (InterruptedException e){}
                } finally {
                    if (isLock) {
                        log.info(finalCount + "开始解锁:" + Thread.currentThread().getName());
                        lock.unlock();
                        log.info(finalCount + "解锁成功:" + Thread.currentThread().getName());
                    }
                }
            }));
        }

        task.forEach(i -> {
            try {
                i.get();
            } catch (Exception e) {}
        });
    }

    private void testLockPerformance(IDistributedLock lock) {
        long seconds = 1;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + seconds * 1000;
        int total = 0;
        do {
            try {
                lock.lock();
            } finally {
                lock.unlock();
            }
            total++;
        } while (System.currentTimeMillis() < endTime);
        log.info("{} 秒上锁解锁 {} 次， 平均1秒上锁解锁 {} 次", seconds, total, total / seconds);
    }

}