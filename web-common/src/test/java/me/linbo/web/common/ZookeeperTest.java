package me.linbo.web.common;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.zookeeper.ZookeeperProperties;

/**
 * @author LinBo
 * @date 2019-11-19 19:41
 */
public class ZookeeperTest {

    private CuratorFramework client;

    @Before
    public void doBefore() {
        ZookeeperProperties zookeeperProperties = new ZookeeperProperties();
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
                zookeeperProperties.getMaxRetries(),
                zookeeperProperties.getMaxSleepMs());
        client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getConnectString())
                .namespace("test-seq")
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @Test
    public void testAdd() throws Exception {
        String zNodeName = "/order-no";
        Stat stat = client.checkExists().forPath(zNodeName);
        if (stat == null) {
            System.out.println("创建节点");
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zNodeName, "0".getBytes());
        } else {
            System.out.println("写值");
            client.setData().forPath(zNodeName, "1".getBytes());
        }
        String val = new String(client.getData().forPath(zNodeName));
        System.out.println(val);
    }
}
