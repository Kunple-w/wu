package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.core.UserService;
import com.github.wu.core.UserServiceImpl;
import com.github.wu.core.rpc.config.ExportConfig;
import com.github.wu.core.rpc.config.RegistryConfig;
import com.github.wu.core.rpc.config.ServiceConfig;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.core.transport.Invocations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BroadcastClusterInvokerTest {

    private static final Logger logger = LoggerFactory.getLogger(BroadcastClusterInvokerTest.class);

    @Test
    void call() {
        Object[] args = new Object[1];
        args[0] = "world";
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hello", args);

        URL registryUrl = URL.of("zookeeper://127.0.0.1:2181");
        ClusterInvoker<UserService> failFastClusterInvoker = new BroadcastClusterInvoker<>(registryUrl, UserService.class);
        failFastClusterInvoker.init();
        ApiResult apiResult = failFastClusterInvoker.call(invocation);
        logger.info("result: {}", apiResult);
    }


    @BeforeEach
    void exportService1() {
        int port = 20000;
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://localhost:2181");
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setHost("localhost");
        serviceConfig.setPort(port);
        serviceConfig.setProtocol("wu");
        ExportConfig<UserService> exportConfig = new ExportConfig<>(UserService.class, new UserServiceImpl(), registryConfig, serviceConfig);
        exportConfig.export();
    }

    @BeforeEach
    void exportService2() {
        int port = 20001;
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://localhost:2181");
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setHost("localhost");
        serviceConfig.setPort(port);
        serviceConfig.setProtocol("wu");
        ExportConfig<UserService> exportConfig = new ExportConfig<>(UserService.class, new UserServiceImpl(), registryConfig, serviceConfig);
        exportConfig.export();
    }
}