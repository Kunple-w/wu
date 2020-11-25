package com.github.wu.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestApplication.class)
class WuConfigurationPropertiesTest {

    @Autowired
    private WuConfigurationProperties wuConfigurationProperties;

    @Test
    public void testProperties() {
        Assertions.assertNotNull(wuConfigurationProperties);
    }
}