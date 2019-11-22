package me.linbo.web.common.sequence.impl;

import me.linbo.web.common.sequence.ISequence;
import me.linbo.web.common.spring.SpringContextHolder;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

/**
 * redis数字序列生成
 * @author LinBo
 * @date 2019-11-22 10:55
 */
public class NumberSequence implements ISequence<Long> {

    private String namespace;

    private long maxValue;

    private long curValue;

    private RedissonClient redissonClient;

    public NumberSequence(String namespace, long maxValue) {
        this.namespace = namespace;
        this.maxValue = maxValue;
        redissonClient = SpringContextHolder.getBean(RedissonClient.class);
    }

    @Override
    public Long next() {
        if (curValue < maxValue) {
            RAtomicLong atomicLong = redissonClient.getAtomicLong(namespace);
            curValue = atomicLong.incrementAndGet();
            if (curValue < maxValue) {
                return curValue;
            }
        }
        return maxValue;
    }
}
