package com.github.wu.core.utils;

import com.github.wu.common.utils.ReflectUtils;
import com.github.wu.core.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

import static com.github.wu.common.utils.ReflectUtils.EMPTY_PARAM;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtilsTest.class);

    @Test
    void getMethodSignature() {
        Map<String, Method> methodListDesc = ReflectUtils.getMethodListDesc(UserService.class);

        for (Map.Entry<String, Method> entry : methodListDesc.entrySet()) {
            String methodSignature = ReflectUtils.getMethodSignature(entry.getValue());
            logger.info("methodSignature: {}", methodSignature);
        }
    }

    @Test
    void getClassName() {
        String className = ReflectUtils.getClassName(UserService.class);
        assertEquals("com.github.wu.core.UserService", className);
    }

    @Test
    void getClassNameVoid() {
        Class<?> aClass = ReflectUtils.getClass(EMPTY_PARAM);
        assertEquals(void.class, aClass);
    }

    @Test
    void getMethodListDesc() {
        Map<String, Method> methodListDesc = ReflectUtils.getMethodListDesc(UserService.class);
        logger.info("map: {}", methodListDesc);
        assertEquals(2, methodListDesc.size());
    }

    @Test
    void testGetClass() {
    }

    @Test
    void getMethod() {
    }

    @Test
    void getClassMethodNames() {
        String methodNames = ReflectUtils.getClassMethodNames(UserService.class);
        logger.info("{}", methodNames);
    }
}