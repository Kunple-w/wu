package com.github.wu.registry.consul;


import com.github.wu.common.URL;
import com.github.wu.registry.api.UrlListener;
import com.githuh.registry.consul.ConsulRegistry;

import java.io.IOException;
import java.util.List;

class ConsulRegistryTest {

    @org.junit.jupiter.api.Test
    void register() {
        ConsulRegistry consulRegistry = new ConsulRegistry();
        consulRegistry.register(getUrl());
    }

    @org.junit.jupiter.api.Test
    void unregister() {
        ConsulRegistry consulRegistry = new ConsulRegistry();
        consulRegistry.unregister(getUrl());
    }

    @org.junit.jupiter.api.Test
    void lookUp() {
        ConsulRegistry consulRegistry = new ConsulRegistry();
        List<URL> urls = consulRegistry.lookup(getUrl());
        assert urls.size() > 0;
    }

    @org.junit.jupiter.api.Test
    void subscribe() throws Exception {
        ConsulRegistry consulRegistry = new ConsulRegistry();
        UrlListener URLListener = new UrlListener() {
            @Override
            public void onEvent(URLChanged context) {
                System.out.println("数据变动: " + context.getNow());
            }
        };
        consulRegistry.subscribe(getUrl(), URLListener);
        System.in.read();
    }


    @org.junit.jupiter.api.Test
    void unsubscribe() throws IOException {
        ConsulRegistry consulRegistry = new ConsulRegistry();
        UrlListener URLListener = new UrlListener() {
            @Override
            public void onEvent(URLChanged context) {
                System.out.println("数据变动: " + context.getNow());
            }
        };
        consulRegistry.subscribe(getUrl(), URLListener);
        consulRegistry.unsubscribe(getUrl(), URLListener);
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
