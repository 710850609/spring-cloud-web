package me.linbo.web.common.sequence.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.sequence.ISequence;
import me.linbo.web.common.spring.SpringContextHolder;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

/**
 * @author LinBo
 * @date 2019-11-22 9:10
 */
@Slf4j
public class RedisSequence implements ISequence<Long> {

    private RedissonClient redissonClient;

    private String sequenceKey;

    public RedisSequence(String sequenceKey) {
        this.sequenceKey = "concurrent:sequence:" + sequenceKey;
        redissonClient = SpringContextHolder.getBean(RedissonClient.class);
        log.info("初始化redis分布式id完成");
    }

    @Override
    public Long next() {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong(this.sequenceKey);
        return atomicLong.incrementAndGet();
    }

}
