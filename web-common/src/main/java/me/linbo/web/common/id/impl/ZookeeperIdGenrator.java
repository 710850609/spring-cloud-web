package me.linbo.web.common.id.impl;

import me.linbo.web.common.id.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 基于Zookeeper产生分布式id
 * @author LinBo
 * @date 2019-11-11 16:54
 */
@Component
public class ZookeeperIdGenrator implements IdGenerator<Long> {


    @Override
    public Long next() {
        return null;
    }
}
