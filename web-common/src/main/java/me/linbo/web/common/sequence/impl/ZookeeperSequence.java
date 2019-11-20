package me.linbo.web.common.sequence.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.lock.DistributedLockException;
import me.linbo.web.common.sequence.ISequence;
import me.linbo.web.common.spring.SpringContextHolder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.util.Strings;
import org.apache.zookeeper.CreateMode;
import org.springframework.cloud.zookeeper.ZookeeperProperties;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于Zookeeper产生分布式id
 * 基于zookeeper永久存储有序节点生成id，每次产生新节点后，删除先前节点
 * @author LinBo
 * @date 2019-11-11 16:54
 */
@Slf4j
public class ZookeeperSequence implements ISequence<Long> {

    /** zk锁命根路径 */
    private static final String ZK_ID_ROOT_PATH = "concurrent/sequence";
    /** zk有序节点前缀 */
    private static final String Z_NODE_PREFIX = "0";
    /** zk有序节点名称 */
    private String zNodeName;

    private static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(1);

    private CuratorFramework client;

    /**
     * 设置zookeeper生成的分布式id路径，如: order-no
     **/
    public ZookeeperSequence(String nodeName) {
        Assert.isTrue(Strings.isNotBlank(nodeName), "zookeeper节点名称不能为空");
        this.zNodeName = "/" + nodeName;
        init();
    }

    private void init() {
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
        log.info("Zookeeper分布式id生成器初始化: " + ZK_ID_ROOT_PATH + this.zNodeName);
        String prefixPath = this.zNodeName + "/" + Z_NODE_PREFIX;
        try {
            // 跳过0值，后续从1开始
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(prefixPath, "0".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long next() {
        try {
            if (client == null) {
                synchronized (this) {
                    init();
                }
            }
            String prefixPath = this.zNodeName + "/" + Z_NODE_PREFIX;
            String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(prefixPath, "0".getBytes());
            String numStr = path.substring(prefixPath.length());
            Long num = Long.valueOf(numStr);

            // 删除先前节点
            FIXED_THREAD_POOL.submit(() -> {
                try {
                    List<String> paths = client.getChildren().forPath(this.zNodeName);
                    for (String p : paths) {
                        if (p.indexOf(Z_NODE_PREFIX) == 0 && num > Long.valueOf(p)) {
                            log.trace("{} 删除节点： {}/{}", path, this.zNodeName, p);
                            client.delete().forPath(this.zNodeName + "/" + p);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("删除zookeeper序列节点失败", e);
                }
            });
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("zookeeper生成分布式id异常", e);
            throw new DistributedLockException(e);
        }
    }

}
