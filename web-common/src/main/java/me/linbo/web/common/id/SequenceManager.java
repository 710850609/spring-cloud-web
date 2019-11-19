package me.linbo.web.common.id;

import me.linbo.web.common.id.impl.SnowFlakeSequence;
import me.linbo.web.common.id.impl.ZookeeperSequence;
import me.linbo.web.common.id.impl.ZookeeperSequencePro;

/**
 * @author LinBo
 * @date 2019-10-15 11:22
 */
public class SequenceManager {

    public static final SnowFlakeSequence SF_ORDER_NO = new SnowFlakeSequence(1, 1);

    public static final ISequence<Long> ZK_ORDER_NO = new ZookeeperSequence("order-no");

    public static final ISequence<Long> ZK_PRO_ORDER_NO = new ZookeeperSequencePro("order-no");
}
