package me.linbo.web.common.lock;

import me.linbo.web.common.lock.impl.RedisDistributedLock;
import me.linbo.web.common.lock.impl.ZkDistributedLock;

/**
 * 分布式锁维护入口
 * @author LinBo
 * @date 2019-11-19 13:33
 */
public interface LockeManager {

    IDistributedLock ZK_ORDER_NO = new ZkDistributedLock("order-no");

    IDistributedLock REDIS_ORDER_NO = new RedisDistributedLock("order-no");
}
