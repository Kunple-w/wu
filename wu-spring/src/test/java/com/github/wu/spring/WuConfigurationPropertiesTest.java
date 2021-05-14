package com.github.wu.spring;

import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(classes = TestApplication.class, properties = "wu.registry.port=52323")
class WuConfigurationPropertiesTest {

    @Autowired
    private WuConfigurationProperties wuConfigurationProperties;
    private static TestingServer server;

    @BeforeAll
    static void setup() throws Exception {
        server = new TestingServer(52323, true);
        server.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        server.close();
    }

    @Test
    public void testProperties() {
        Assertions.assertNotNull(wuConfigurationProperties);
    }
}