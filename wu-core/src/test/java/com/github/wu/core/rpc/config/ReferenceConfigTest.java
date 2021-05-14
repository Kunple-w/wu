package com.github.wu.core.rpc.config;

import com.github.wu.core.UserService;
import com.github.wu.core.UserServiceImpl;
import com.github.wu.core.rpc.filter.FilterRegistry;
import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReferenceConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceConfigTest.class);
    private int port = 20000;
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
    public void test() throws Exception {
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://localhost:" + zkPort);
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setHost("localhost");
        serviceConfig.setPort(port);
        serviceConfig.setProtocol("wu");
        ExportConfig<UserService> exportConfig = new ExportConfig<>(UserService.class, new UserServiceImpl(), registryConfig, serviceConfig, new FilterRegistry());
        exportConfig.export();

        ReferenceConfig<UserService> referenceConfig = new ReferenceConfig<>(UserService.class, registryConfig, new FilterRegistry());
        UserService userService = referenceConfig.refer();

        assertEquals("hello world", userService.hello("world"), "远程调用失败");
    }
}