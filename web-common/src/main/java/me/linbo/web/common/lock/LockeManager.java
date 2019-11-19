package me.linbo.web.common.lock;

import me.linbo.web.common.lock.impl.ZkDistributedLock;

/**
 * @author LinBo
 * @date 2019-11-19 13:33
 */
public class LockeManager {

    private static final ZkDistributedLock ZK_ORDER_NO = new ZkDistributedLock() {
        @Override
        public String getLockPath() {
            return null;
        }
    };

}
