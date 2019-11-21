package me.linbo.web.common.sequence;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SequenceManagerTest {

    @Test
    public void testZk() throws Exception {
        // 触发一次，进行zk预热
        SequenceManager.ZK_ORDER_NO.next();
        testBase(SequenceManager.ZK_ORDER_NO);
        // 线程等待异步删除zk节点
        Thread.sleep(5000L);
    }

    @Test
    public void testZkpRro() throws Exception {
        // 触发一次，进行zk预热
        SequenceManager.ZK_PRO_ORDER_NO.next();
        testBase(SequenceManager.ZK_PRO_ORDER_NO);
        Thread.sleep(5000L);
    }

    @Test
    public void testSf() throws Exception {
        testBase(SequenceManager.SF_ORDER_NO);
    }

    private void testBase(ISequence<Long> sequence) throws Exception {
        int processors = Runtime.getRuntime().availableProcessors();
        int count = processors;
        long seconds = 1;
        ExecutorService executor = Executors.newFixedThreadPool(count);
        long startTime = System.currentTimeMillis();
        long endTime = startTime + seconds * 1000;
        List<Future<Integer>> tasks = new ArrayList<>(processors);
        while (count-- > 0) {
            Future<Integer> submit = executor.submit(() -> {
                int cur = 0;
                do {
                    Long next = sequence.next();
                    cur++;
                } while (System.currentTimeMillis() < endTime);
                log.info("{} 秒产生 {} 个", seconds, (cur));
                return cur;
            });
            tasks.add(submit);
        }
        Integer total = 0;
        for (Future<Integer> f : tasks) {
            total += f.get();
        }
        if (total > 10000) {
            log.info("{} 秒，{} 线程，生成序列值 {} w个", seconds, processors, (total / 10000));
        } else {
            log.info("{} 秒，{} 线程，生成序列值 {} 个", seconds, processors, (total));
        }
        if (total / seconds / processors > 10000) {
            log.info("平均单线程1秒产生序列值 {} w个", total / seconds / processors / 10000);
        } else {
            log.info("平均单线程1秒产生序列值 {} 个", total / seconds / processors);
        }

    }

}