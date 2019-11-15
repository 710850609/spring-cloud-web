package me.linbo.web.common.lock.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZkDistributedLockTest {

    @Autowired
    private OrderZkLock lock;

    @Test
    public void lock() {
    }

    @Test
    public void lockInterruptibly() {
    }

    @Test
    public void tryLock() {
        int count = 5;

        ExecutorService executor = Executors.newFixedThreadPool(8);
        ArrayList<Future> task = new ArrayList<>(count);
        while (count-- > 0) {
            int finalCount = count;
            task.add(executor.submit(() -> {
                try {
                    lock.lock();
                    System.out.println(finalCount + "上锁:" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(60000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    lock.unlock();
                    System.out.println(finalCount + "解锁:" + Thread.currentThread().getName());
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

    @Test
    public void testTryLock() throws Exception {
        lock.tryLock(500L);
    }

    @Test
    public void unlock() {
    }
}