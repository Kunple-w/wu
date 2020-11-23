package com.github.wu.core.transport;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ServerTest {

    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);

    @Test
    void start() throws InterruptedException {
        logger.info("server start");
        Server server = new Server(new InetSocketAddress(13232));
        server.start();
        TimeUnit.HOURS.sleep(100);
    }

}