package me.linbo.web.common.sequence;

import me.linbo.web.common.sequence.impl.SnowFlakeSequence;
import me.linbo.web.common.sequence.impl.ZookeeperSequence;
import me.linbo.web.common.sequence.impl.ZookeeperSequencePro;

/**
 * 分布式序列维护入口
 * @author LinBo
 * @date 2019-10-15 11:22
 */
public class SequenceManager {

    public static final SnowFlakeSequence SF_ORDER_NO = new SnowFlakeSequence(1, 1);

    public static final ISequence<Long> ZK_ORDER_NO = new ZookeeperSequence("order-no");

    public static final ISequence<Long> ZK_PRO_ORDER_NO = new ZookeeperSequencePro("order-no");
}
