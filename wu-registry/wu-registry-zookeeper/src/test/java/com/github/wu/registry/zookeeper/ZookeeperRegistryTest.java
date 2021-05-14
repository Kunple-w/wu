package com.github.wu.registry.zookeeper;

import com.github.wu.common.URL;
import com.github.wu.registry.api.UrlListener;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZookeeperRegistryTest {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistryTest.class);
    public static String connectString = "";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

    CuratorFramework client;
    private int port = -1;
    private static TestingServer server;

    @BeforeEach
    void setupServer() throws Exception {
        server = new TestingServer(-1, true);
        server.start();
        port = server.getPort();
        connectString = "127.0.0.1:" + port;
        client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    }

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
    }

    @org.junit.jupiter.api.Test
    @Timeout(30)
    void register() {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        registry.register(url);
    }


    @org.junit.jupiter.api.Test
    @Timeout(30)
    void unregister() {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        registry.register(url);
        registry.unregister(url);
    }

    @org.junit.jupiter.api.Test
    @Timeout(30)
    void lookup() {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        registry.register(url);
        List<URL> lookup = registry.lookup(url);
        assertEquals(1, lookup.size());
    }

    @org.junit.jupiter.api.Test
    @Timeout(30)
    void subscribe() throws InterruptedException {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        CountDownLatch latch = new CountDownLatch(1);
        registry.subscribe(url, new UrlListener() {
            @Override
            public void onEvent(URLChanged context) {
                latch.countDown();
                logger.info("数据变动: {}", context.getNow());
            }
        });
        registry.register(url);
        latch.await();
    }

    @org.junit.jupiter.api.Test
    @Timeout(30)
    void unsubscribe() throws InterruptedException {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        CountDownLatch latch = new CountDownLatch(1);

        registry.subscribe(url, new UrlListener() {
            @Override
            public void onEvent(URLChanged context) {
                latch.countDown();
                logger.info("数据变动: {}", context.getNow());
            }
        });
        registry.register(url);
        assertEquals(1, registry.lookup(url).size());
        registry.unregister(url);
        assertEquals(0, registry.lookup(url).size());
        latch.await();
    }

    protected URL getUrl() {
        return URL.of(("wu://10.180.204.199:20880\n" +
                "/org.apache.dubbo.demo.DemoService?\n" +
                "anyhost=true\n" +
                "&application=wu-demo-api-provider\n" +
                "&bind.ip=10.180.204.199\n" +
                "&bind.port=20880\n" +
                "&default=true\n" +
                "&deprecated=false\n" +
                "&dubbo=2.0.2\n" +
                "&dynamic=true\n" +
                "&generic=false\n" +
                "&interface=com.github.wu.spring.biz.AdminService\n" +
                "&methods=admin\n" +
                "&pid=22692\n" +
                "&release=\n" +
                "&side=provider\n" +
                "&timestamp=1603277027790").replaceAll("\n", ""));
    }

}