package com.github.wu.core.rpc.remoting.transport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);

    private Server server;
    private Client client;
    private int serverPort = 13232;


    @Test
    void remote() throws InterruptedException {
        client = new Client(new InetSocketAddress(serverPort));
        client.start();
        TimeUnit.SECONDS.sleep(2);
    }

    @BeforeEach
    void startServer() throws InterruptedException {
        logger.info("server start");
        server = new Server(new InetSocketAddress(serverPort));
        server.start();
        TimeUnit.SECONDS.sleep(2);

    }

    @AfterEach
    void tearDown() {
        client.disConnect();
        server.stop();
    }


}