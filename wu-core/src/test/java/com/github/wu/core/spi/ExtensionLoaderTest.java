package com.github.wu.core.spi;

import com.github.wu.common.spi.ExtensionLoader;
import com.github.wu.core.UserService;
import com.github.wu.core.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtensionLoaderTest {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoaderTest.class);

    @Test
    void getExtension() {
        ExtensionLoader<UserService> extensionLoader = ExtensionLoader.getExtensionLoader(UserService.class);
        UserService userService = extensionLoader.getExtension("userServiceImpl");
        assertEquals(UserServiceImpl.class, userService.getClass(), "获取扩展类错误");
    }
}