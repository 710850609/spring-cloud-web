package me.linbo.web.common.sequence.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.sequence.DistributedSequenceException;
import me.linbo.web.common.sequence.ISequence;
import me.linbo.web.common.spring.SpringContextHolder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.util.Strings;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.cloud.zookeeper.ZookeeperProperties;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于zookeeper实现分布式id生成，带有本地缓存
 * @author LinBo
 * @date 2019-11-19 10:12
 */
@Slf4j
public class ZookeeperSequencePro implements ISequence<Long> {

    /** zookeeper锁跟节点名称 */
    private static final String ZK_ID_ROOT_PATH = "concurrent/sequence";
    /** zookeeper锁节点名称 */
    private String zNodeName;

    private static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(1);

    private CuratorFramework client;

    private InterProcessLock interProcessLock;

    /**
     * 设置zookeeper生成的分布式id路径，如: order-no
     **/
    public ZookeeperSequencePro(String nodeName) {
        Assert.isTrue(Strings.isNotBlank(nodeName), "zookeeper节点名称不能为空");
        this.zNodeName = "/" + nodeName;
        init();

    }

    /** 本地缓存当前值 */
    private long curNum = 0;
    /** 本地缓存最大值 */
    private long maxNum = 0;
    /** 批次获取数量 */
    private static final long PERIOD = 1000;

    @Override
    public synchronized Long next() {
        // 从本地缓存中取
        if (curNum == 0 || curNum >= maxNum - 1) {
            fetchSegment();
        }
        long num = curNum++;
        if (maxNum - curNum < (PERIOD / 5)) {
            // 如果缓存量低于缓存批次的20%，则提前异步批量获取进行缓存
            FIXED_THREAD_POOL.submit(() -> fetchSegment());
        }
        return num;
    }

    private synchronized void init() {
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
        // 创建不可重入互斥锁
        this.interProcessLock = new InterProcessSemaphoreMutex(client, zNodeName);
        log.info("Zookeeper分布式id生成器初始化: /{}{}", ZK_ID_ROOT_PATH, zNodeName);
        fetchSegment();
    }

    private synchronized void fetchSegment() {
        try {
            this.interProcessLock.acquire();
            Stat stat = client.checkExists().forPath(zNodeName);
            if (stat == null) {
                log.trace("创建zookeeper分布式id节点: {}", zNodeName);
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zNodeName, String.valueOf(PERIOD).getBytes());
                maxNum = PERIOD;
                curNum = 0;
            } else {
                String str = new String(client.getData().forPath(zNodeName));
                long curVal = 0;
                if (Strings.isNotBlank(str)) {
                    curVal = Long.parseLong(str);
                }
                // 如果当前序列值不是初始化值，则回写zk上的值
                if (curNum == 0) {
                    log.trace("读取zookeeper分布式id节点值: {}:{}", zNodeName, curVal);
                    curNum = curVal;
                }
                long nextVal = curVal + PERIOD;
                client.setData().forPath(zNodeName, String.valueOf(nextVal).getBytes());
                maxNum = nextVal;
                log.trace("设置zookeeper节点最大序列值: {}:{}", zNodeName, nextVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("zookeeper写入序列系节点失败", e);
            throw new DistributedSequenceException(e);
        } finally {
            try {
                this.interProcessLock.release();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("释放zookeeper节点锁失败", e);
            }
        }
    }

}
