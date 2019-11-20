package me.linbo.web.common.lock;

import me.linbo.web.common.lock.impl.ZkDistributedLock;

/**
 * 分布式锁维护入口
 * @author LinBo
 * @date 2019-11-19 13:33
 */
public class LockeManager {

    public static final ZkDistributedLock ZK_ORDER_NO = new ZkDistributedLock("order-no");

}