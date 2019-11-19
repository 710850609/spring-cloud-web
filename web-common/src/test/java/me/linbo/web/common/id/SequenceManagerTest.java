package me.linbo.web.common.id;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SequenceManagerTest {

    @Test
    public void testZk() throws InterruptedException {
        testBase(SequenceManager.ZK_ORDER_NO);
        // 线程等待异步删除zk节点
        Thread.sleep(60000L);
    }

    @Test
    public void testZkpRro() throws InterruptedException {
        testBase(SequenceManager.ZK_PRO_ORDER_NO);
        // 线程等待异步删除zk节点
        Thread.sleep(60000L);
    }

    @Test
    public void testSf() throws InterruptedException {
        testBase(SequenceManager.SF_ORDER_NO);
    }

    private void testBase(ISequence<Long> sequence) throws InterruptedException {
        int processors = Runtime.getRuntime().availableProcessors();
        int count = processors;
        long seconds = 10;
        ExecutorService executor = Executors.newFixedThreadPool(count);
        long startTime = System.currentTimeMillis();
        AtomicLong total = new AtomicLong();
        CountDownLatch countDownLatch = new CountDownLatch(count);
        while (count-- > 0) {
            executor.submit(() -> {
                int cur = 0;
                do {
                    Long next = sequence.next();
                    cur++;
                } while (System.currentTimeMillis() - startTime < seconds * 1000);
                log.info("{} 秒产生 {} 个", seconds, (cur));
                total.addAndGet(cur);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        if (total.get() > 10000) {
            log.info("{} 秒，{} 线程，生成序列值 {} 个", seconds, processors, (total.get()));
            log.info("平均单线程1秒产生序列值 {} 个", total.get() / seconds / processors);
        } else {
            log.info("{} 秒，{} 线程，生成序列值 {} 个", seconds, processors, (total.get()));
            log.info("平均单线程1秒产生序列值 {} 个", total.get() / seconds / processors);
        }
    }
}