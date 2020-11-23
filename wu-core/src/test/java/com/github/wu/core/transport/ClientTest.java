package com.github.wu.core.transport;

import com.github.wu.core.UserService;
import com.github.wu.core.rpc.Reference;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);

    @Test
    void start() throws InterruptedException {
        Client client = new Client(new InetSocketAddress(13232));
        client.start();
        TimeUnit.SECONDS.sleep(2);
        logger.info("channel: {}", client.getChannel());


        for (int i = 0; i < 2; i++) {
            SengMessage sengMessage = new SengMessage();
            sengMessage.setHeader(new SengProtocolHeader());
            sengMessage.setBody(new byte[0]);
//            client.send(sengMessage);
        }
        TimeUnit.HOURS.sleep(100);
    }

    @Test
    void remote() throws InterruptedException {
        Client client = new Client(new InetSocketAddress(13232));
        client.start();
        TimeUnit.SECONDS.sleep(2);

        Reference<UserService> reference = new Reference<>(client, UserService.class);
        UserService refer = reference.refer();
        String hello = refer.hello("remote, ");
        logger.info("result: {}", hello);
    }
}