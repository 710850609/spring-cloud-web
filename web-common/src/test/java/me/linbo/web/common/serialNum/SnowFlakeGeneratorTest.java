package me.linbo.web.common.serialNum;


import me.linbo.web.common.id.impl.SnowFlakeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public class SnowFlakeGeneratorTest {

    private ThreadPoolTaskExecutor executor;

    @Before
    public void doBefore() {
        executor= new ThreadPoolTaskExecutor();
        executor.setKeepAliveSeconds(1);
        executor.setCorePoolSize(100);
        executor.initialize();
    }

    @Test
    public void next() {
        SnowFlakeGenerator gen = new SnowFlakeGenerator(1, 3);
        System.out.println(gen.next());
        long startTime = System.currentTimeMillis();
        Set<Long> result = new HashSet<>();
        int i = 8;
        List<Future<Integer>> futures = new ArrayList<>(i);
        while (i-- > 0) {
            Future<Integer> submit = executor.submit(() -> {
                int count = 0;
                do {
                    Long next = gen.next();
                    if (result.contains(next)) {
                        throw new IllegalArgumentException("id重复" + next);
                    }
//                    System.out.println(Thread.currentThread().getName() + ": " + next);
                    result.add(next);
                    count++;
                } while (System.currentTimeMillis() - startTime < 1000);
                System.out.println(Thread.currentThread().getName() + "1秒产生" + (count / 1000) + "k个");
                return count;
            });
            futures.add(submit);
        }
        futures.forEach(v -> {
            try {
                v.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("产生" + (result.size() / 1000) + "k个");
    }
}