package me.linbo.web.common;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.lock.IDistributedLock;
import me.linbo.web.common.spring.SpringContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author LinBo
 * @date 2019-11-21 11:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTemplateTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testAdd() {
        String key = "concurrent:lock:order-no";
        redisTemplate.opsForValue().set(key, UUID.randomUUID().toString());
        Object val = redisTemplate.opsForValue().get(key);
        log.info("value: {}", val);
    }

    @Test
    public void testDelete() {
        Boolean delete = redisTemplate.delete("concurrent:lock:order-no");
        System.out.println(delete);
    }


    @Test
    public void testRedisAtomicLongPerformance() throws ExecutionException, InterruptedException {
        int seconds = 10;
        int processors = Runtime.getRuntime().availableProcessors();
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong("test:sequence:RedisAtomicLong-no", redisTemplate.getConnectionFactory());
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        ArrayList<Future<Long>> task = new ArrayList<>(processors);
        int count = processors;
        while (count-- > 0) {
            long endTime = System.currentTimeMillis() + seconds * 1000;
            task.add(executor.submit(() -> {
                long total = 0;
                while (endTime > System.currentTimeMillis()) {
                    redisAtomicLong.incrementAndGet();
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

    public static void main(String[] args) throws IOException {
        // 测试默认redisTemplate乱码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        String object = "test";
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        byte[] bytes = outputStream.toByteArray();
        System.out.println(Arrays.toString(object.getBytes()));
        System.out.println(Arrays.toString(bytes));
        System.out.println(new String(bytes, "UTF-8"));
    }
}
