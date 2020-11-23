package com.github.wu.registry.zookeeper;

import com.github.wu.common.URL;
import com.github.wu.registry.api.EventListener;
import com.github.wu.registry.api.LocalRegisterService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZookeeperRegistryTest {

    public static final String connectString = "127.0.0.1:2181";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

    CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);


    @BeforeEach
    void setup() {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        List<URL> lookup = registry.lookup(url);
        if (!lookup.isEmpty()) {
            registry.unregister(url);
        }
    }

    @org.junit.jupiter.api.Test
    void register() {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        registry.register(url);
    }


    @org.junit.jupiter.api.Test
    void unregister() {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        registry.register(url);
        registry.unregister(url);
    }

    @org.junit.jupiter.api.Test
    void lookup() {
        client.start();
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        registry.register(url);
        List<URL> lookup = registry.lookup(url);
        assertEquals(1, lookup.size());
    }

    @org.junit.jupiter.api.Test
    void subscribe() throws IOException {

        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        registry.register(url);
        registry.subscribe(url, new EventListener() {
            @Override
            public void onEvent(LocalRegisterService.URLChanged context) {
                System.out.println("数据变动: " + context.getNow());
            }
        });
        System.in.read();
    }

    @org.junit.jupiter.api.Test
    void unsubscribe() throws IOException {
        ZookeeperRegistry registry = new ZookeeperRegistry(client);
        URL url = getUrl();
        registry.register(url);
        EventListener eventListener = new EventListener() {
            @Override
            public void onEvent(LocalRegisterService.URLChanged context) {
                System.out.println("数据变动: " + context.getNow());
            }
        };
        registry.subscribe(url, eventListener);
        registry.unsubscribe(url, eventListener);
        System.in.read();
    }

    protected URL getUrl() {
        URL url = URL.of(("dubbo://10.180.204.199:20880\n" +
                "/org.apache.dubbo.demo.DemoService?\n" +
                "anyhost=true\n" +
                "&application=dubbo-demo-api-provider\n" +
                "&bind.ip=10.180.204.199\n" +
                "&bind.port=20880\n" +
                "&default=true\n" +
                "&deprecated=false\n" +
                "&dubbo=2.0.2\n" +
                "&dynamic=true\n" +
                "&generic=false\n" +
                "&interface=org.apache.dubbo.demo.DemoService\n" +
                "&methods=sayHello,sayHelloAsync\n" +
                "&pid=22692\n" +
                "&release=\n" +
                "&side=provider\n" +
                "&timestamp=1603277027790").replaceAll("\n", ""));
        return url;
    }
}