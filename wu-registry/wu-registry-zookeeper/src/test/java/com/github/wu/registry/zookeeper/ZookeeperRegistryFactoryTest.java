package com.github.wu.registry.zookeeper;

import com.github.wu.common.URL;
import com.github.wu.registry.api.RegisterService;
import com.github.wu.registry.api.RegistryFactory;
import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

class ZookeeperRegistryFactoryTest {

    private static int port = -1;
    private static TestingServer server;

    @BeforeAll
    static void setup() throws Exception {
        server = new TestingServer(-1, true);
        server.start();
        port = server.getPort();
    }

    @AfterAll
    static void tearDown() throws IOException {
        server.stop();
    }

    @Test
    void createRegistry() {
        String a = "zookeeper://localhost:" + port;
        RegistryFactory zookeeperRegistryFactory = new ZookeeperRegistryFactory();
        RegisterService registry = zookeeperRegistryFactory.getRegistry(URL.of(a));
        List<URL> lookup = registry.lookup(URL.of(a));
        Assertions.assertEquals(Collections.emptyList(), lookup, "look up is not empty");
    }
}