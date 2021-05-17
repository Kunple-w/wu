package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.core.UserService;
import com.github.wu.core.UserServiceImpl;
import com.github.wu.core.config.ExportConfig;
import com.github.wu.core.config.RegistryConfig;
import com.github.wu.core.config.ServiceConfig;
import com.github.wu.core.rpc.remoting.filter.FilterRegistry;
import com.github.wu.core.rpc.remoting.transport.ApiResult;
import com.github.wu.core.rpc.remoting.transport.Invocation;
import com.github.wu.core.rpc.remoting.transport.Invocations;
import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class FailFastClusterInvokerTest {
    private static final Logger logger = LoggerFactory.getLogger(FailFastClusterInvokerTest.class);
    private static TestingServer server;
    private static int zkPort = -1;

    @BeforeEach
    void setup() throws Exception {
        server = new TestingServer(true);
        server.start();
        zkPort = server.getPort();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.close();
    }

    @Test
    void call() {
        Object[] args = new Object[1];
        args[0] = "world";
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hello", args);

        URL registryUrl = URL.of("zookeeper://127.0.0.1:" + zkPort);
        FailFastClusterInvoker<UserService> failFastClusterInvoker = new FailFastClusterInvoker<>(registryUrl, UserService.class);
        failFastClusterInvoker.init();
        ApiResult apiResult = failFastClusterInvoker.call(invocation);
        Assertions.assertEquals("hello world", apiResult.getValue());
    }


    @BeforeEach
    void exportService1() {
        int port = 20000;
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://localhost:" + zkPort);
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setHost("localhost");
        serviceConfig.setPort(port);
        serviceConfig.setProtocol("wu");
        ExportConfig<UserService> exportConfig = new ExportConfig<>(UserService.class, new UserServiceImpl(), registryConfig, serviceConfig, new FilterRegistry());
        exportConfig.export();
    }

    @BeforeEach
    void exportService2() {
        int port = 20001;
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://localhost:" + zkPort);
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setHost("localhost");
        serviceConfig.setPort(port);
        serviceConfig.setProtocol("wu");
        ExportConfig<UserService> exportConfig = new ExportConfig<>(UserService.class, new UserServiceImpl(), registryConfig, serviceConfig, new FilterRegistry());
        exportConfig.export();
    }

}