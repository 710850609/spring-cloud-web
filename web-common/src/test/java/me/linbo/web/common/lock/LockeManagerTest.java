package me.linbo.web.common.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
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

    private IDistributedLock lock;

    @Before
    public void doBefore() {
        lock = LockeManager.ZK_ORDER_NO;
    }

    @Test
    public void lock() {
        int count = 10;
        ExecutorService executor = Executors.newFixedThreadPool(8);
        ArrayList<Future> task = new ArrayList<>(count);
        while (count-- > 0) {
            int finalCount = count;
            task.add(executor.submit(() -> {
                boolean isLock = true;
                try {
                    lock.lock();
//                    isLock = lock.tryLock();
                    log.info(finalCount + "上锁:" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    if (isLock) {
                        log.info(finalCount + "解锁:" + Thread.currentThread().getName());
                        lock.unlock();
                    }
                }
            }));
        }

        task.forEach(i -> {
            try {
                i.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}