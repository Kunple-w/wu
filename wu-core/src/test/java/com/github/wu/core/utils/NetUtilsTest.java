package com.github.wu.core.utils;

import com.github.wu.common.utils.NetUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

class NetUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(NetUtilsTest.class);

    @Test
    void getLocalHost() {
        String localHost = NetUtils.getLocalHost();
        logger.info("{}", localHost);
    }

    @Test
    void getLocalAddress() {
        InetAddress inetAddress = NetUtils.getLocalAddress();
        logger.info("{}", inetAddress);
    }
}