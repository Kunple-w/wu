package com.github.wu.common.spi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ExtensionLoaderTest {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoaderTest.class);

    @Test
    void getExtension() {
        ExtensionLoader<NameGenerator> extensionLoader = ExtensionLoader.getExtensionLoader(NameGenerator.class);
        NameGenerator nameGenerator = extensionLoader.getExtension("DefaultNameGenerator");
        Assertions.assertEquals(DefaultNameGenerator.class, nameGenerator.getClass(), "获取扩展类错误");
    }
}