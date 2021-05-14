package com.github.wu.registry.consul;


import com.github.wu.common.URL;
import com.github.wu.registry.api.UrlListener;
import com.githuh.registry.consul.ConsulRegistry;
import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class ConsulRegistryTest {
    private static ConsulProcess consul;
    private static String url = "";


    @BeforeEach
    void setup() {
        this.consul = ConsulStarterBuilder
                .consulStarter()
                .withCustomConfig("{\"enable_script_checks\":true}")
                .build()

                .start();
        int port = consul.getHttpPort();
        url = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        consul.close();
    }


    @org.junit.jupiter.api.Test
    void register() {
        ConsulRegistry consulRegistry = new ConsulRegistry(url);
        consulRegistry.register(getUrl());
    }

    @org.junit.jupiter.api.Test
    void unregister() {
        ConsulRegistry consulRegistry = new ConsulRegistry(url);
        consulRegistry.unregister(getUrl());
    }

    @org.junit.jupiter.api.Test
    void lookUp() throws InterruptedException {
        ConsulRegistry consulRegistry = new ConsulRegistry(url);
        consulRegistry.register(getUrl());
        TimeUnit.SECONDS.sleep(2);
        List<URL> urls = consulRegistry.lookup(getUrl());
        Assertions.assertEquals(1, urls.size());
    }

    @org.junit.jupiter.api.Test()
    @Timeout(30)
    @Disabled
    void subscribe() throws Exception {
        ConsulRegistry consulRegistry = new ConsulRegistry(url);
        CountDownLatch latch = new CountDownLatch(1);
        consulRegistry.subscribe(getUrl(), new UrlListener() {
            @Override
            public void onEvent(URLChanged context) {
                latch.countDown();
                System.out.println("数据变动: " + context.getNow());
            }
        });
        consulRegistry.unregister(getUrl());
        latch.await();
    }


    @org.junit.jupiter.api.Test
    void unsubscribe() throws IOException {
        ConsulRegistry consulRegistry = new ConsulRegistry(url);
        UrlListener URLListener = new UrlListener() {
            @Override
            public void onEvent(URLChanged context) {
                System.out.println("数据变动: " + context.getNow());
            }
        };
        consulRegistry.subscribe(getUrl(), URLListener);
        consulRegistry.unsubscribe(getUrl(), URLListener);
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
