package me.linbo.web.common.id.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.id.ISequence;
import me.linbo.web.common.spring.SpringContextHolder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.zookeeper.ZookeeperProperties;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于zookeeper实现分布式id生成
 * @author LinBo
 * @date 2019-11-19 10:12
 */
@Slf4j
public class ZookeeperSequencePro implements ISequence<Long> {

    /** zookeeper锁跟节点名称 */
    private static final String ZK_ID_ROOT_PATH = "concurrent-id";
    /** zookeeper锁节点名称 */
    private String zNodeName;
    /**  */
    private static final String ZNODE_PREFIX = "seq-";

    private static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(1);

    private CuratorFramework client;


    /**
     * 设置zookeeper生成的分布式id路径，如: order-no
     **/
    public ZookeeperSequencePro(String nodeName) {
        Assert.isTrue(Strings.isNotBlank(nodeName), "zookeeper节点名称不能为空");
        this.zNodeName = "/" + nodeName;
    }

    /** 本地缓存当前值 */
    private long curNum = 0;
    /** 本地婚车最大值 */
    private long maxNum = 0;
    /** 批次获取数量 */
    private static final long PERIOD = 1000;

    @Override
    public Long next() {
        // 从本地缓存中取

        // 如果缓存量低于阈值，则提前异步批量获取进行缓存

        return null;
    }

    public void init() {
        ZookeeperProperties zookeeperProperties = SpringContextHolder.getBean(ZookeeperProperties.class);
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
                zookeeperProperties.getMaxRetries(),
                zookeeperProperties.getMaxSleepMs());
        client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getConnectString())
                .namespace(ZK_ID_ROOT_PATH)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        log.info("Zookeeper分布式id生成器初始化: " + ZK_ID_ROOT_PATH + zNodeName);
        // 从zookeeper读取当前值，并缓存id段
    }

}
