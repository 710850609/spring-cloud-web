package me.linbo.web.common.sequence;

import me.linbo.web.common.sequence.impl.RedisSequence;
import me.linbo.web.common.sequence.impl.SnowFlakeSequence;
import me.linbo.web.common.sequence.impl.ZookeeperSequence;
import me.linbo.web.common.sequence.impl.ZookeeperSequencePro;
import me.linbo.web.common.sequence.impl.NumberCacheSequence;

/**
 * 分布式序列维护入口
 * @author LinBo
 * @date 2019-10-15 11:22
 */
public interface SequenceManager {

    ISequence<Long> SF_ORDER_NO = new SnowFlakeSequence(1, 1);

    ISequence<Long> ZK_ORDER_NO = new ZookeeperSequence("order-no");

    ISequence<Long> ZK_PRO_ORDER_NO = new ZookeeperSequencePro("order-no");
    
    ISequence<Long> REDIS_ORDER_NO = new RedisSequence("order-no");

    ISequence<Long> REDIS_CACHE_ORDER_NO = new NumberCacheSequence("order-no");

}
