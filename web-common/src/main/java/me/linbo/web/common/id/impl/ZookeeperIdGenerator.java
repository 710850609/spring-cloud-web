package me.linbo.web.common.id.impl;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.common.id.IdGenerator;
import me.linbo.web.common.lock.DistributedLockException;
import me.linbo.web.common.spring.SpringContextHolder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.cloud.zookeeper.ZookeeperProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于Zookeeper产生分布式id
 * @author LinBo
 * @date 2019-11-11 16:54
 */
@Component
@Slf4j
public abstract class ZookeeperIdGenerator implements IdGenerator<Long> {

    private static final String ZK_ID_ROOT_PATH = "concurrent-id";

    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

    private CuratorFramework client;

    /**
     * 设置zookeeper生成的分布式id路径，如: /order-no
     * @Author LinBo
     * @Date 2019-11-18 15:37
     * @return {@link String}
     **/
    abstract String getIdPath();

    @Override
    public Long next() {
        try {
            if (client == null) {
                initClient();
            }
            String prefixPath = getIdPath() + "/seq-";
            String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(prefixPath, "0".getBytes());
            log.info("生成节点： {}", path);
            return Long.valueOf(path.substring(prefixPath.length()));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("zookeeper生成分布式id异常", e);
            throw new DistributedLockException(e);
        }
    }

    public void initClient() {
        Objects.requireNonNull(getIdPath(), "锁节点名称不能为空");
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
        log.info("Zookeeper分布式id生成器初始化: " + ZK_ID_ROOT_PATH + getIdPath());
    }

}
