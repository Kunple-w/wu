package com.github.wu.core.rpc;

import com.github.wu.common.URL;
import com.github.wu.core.UserService;
import com.github.wu.core.UserServiceImpl;
import com.github.wu.core.rpc.filter.FilterRegistry;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.core.transport.Invocations;
import com.github.wu.core.transport.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

class RemoteInvokerTest {
    private static final Logger logger = LoggerFactory.getLogger(RemoteInvokerTest.class);
    private int port = 18100;

    @Test
    void call() {
        String a = "wu://localhost:18100/com.github.wu.core.UserService?methods=hello,search,hi,hi";
        URL url = URL.of(a);
        Object[] args = new Object[1];
        args[0] = "world2";
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hello", args);
        RemoteInvoker<UserService> invoker = new RemoteInvoker<>(url, UserService.class);
        invoker.init();
        ApiResult apiResult = invoker.call(invocation);
        logger.info("apiResult: {}", apiResult);
    }

    @BeforeEach
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

    @AfterEach
    void tearDown() {

    }
}