package me.linbo.web.common.id.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.lock.impl.OrderZkLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ZookeeperIdGeneratorTest {

    private OrderNo orderNo = new OrderNo();

    @Test
    public void next() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        int count = 10;
        while (count-- > 0) {
            service.submit(() -> {
                Long next = orderNo.next();
                log.info(next.toString());
            });
        }
        Thread.sleep(10000L);
    }


}

class OrderNo extends ZookeeperIdGenerator {

    @Override
    String getIdPath() {
        return "/order-no";
    }
}