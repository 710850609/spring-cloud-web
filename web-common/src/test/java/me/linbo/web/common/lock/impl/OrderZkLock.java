package me.linbo.web.common.lock.impl;

import org.springframework.stereotype.Component;

/**
 * @author LinBo
 * @date 2019-11-12 14:01
 */
@Component
public class OrderZkLock extends ZkDistributedLock {

    private static final String LOCK_PATH = "/orders";

    @Override
    public String getLockPath() {
        return LOCK_PATH;
    }
}
