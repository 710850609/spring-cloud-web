package me.linbo.web.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.UUID;

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

    @Before
    public void doBefore() {
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    }


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
