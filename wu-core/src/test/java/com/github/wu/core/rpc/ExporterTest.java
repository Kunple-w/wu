package com.github.wu.core.rpc;

import com.github.wu.common.URL;
import com.github.wu.core.UserService;
import com.github.wu.core.UserServiceImpl;
import com.github.wu.core.rpc.filter.FilterRegistry;
import com.github.wu.core.transport.Client;
import com.github.wu.core.transport.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

class ExporterTest {
    private static final Logger logger = LoggerFactory.getLogger(ExporterTest.class);

    private int port = 18000;

    void export() throws InterruptedException {
        Server server = new Server(new InetSocketAddress(port));
        server.start();
        UserServiceImpl userService = new UserServiceImpl();
        Exporter<UserService> exporter = new Exporter<>(UserService.class, userService, new FilterRegistry());
        exporter.setProtocol("wu");
        exporter.setPort(port);
        exporter.setServer(server);

        exporter.export();
        URL url = exporter.getURL();
        logger.info("url: {}", url.getFullURL());
    }

    @Test
    void reference() throws Exception {

        export();

        Client client = new Client(new InetSocketAddress(port));
        client.start();
        TimeUnit.SECONDS.sleep(2);
        Reference<UserService> reference = new Reference<>(client, UserService.class);
        UserService refer = reference.refer();
        String hello = refer.hello("wu!");
        logger.info("result: {}", hello);
        Assertions.assertEquals("hello wu!", hello);
    }

    @Test
    void testExport() {
    }

    @Test
    void testExport1() {
    }

    @Test
    void register() {
    }

    @Test
    void testExport2() {
    }

    @Test
    void unExport() {
    }
}