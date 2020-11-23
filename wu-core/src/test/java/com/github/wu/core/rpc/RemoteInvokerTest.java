package com.github.wu.core.rpc;

import com.github.wu.common.URL;
import com.github.wu.core.UserService;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.core.transport.Invocations;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RemoteInvokerTest {
    private static final Logger logger = LoggerFactory.getLogger(RemoteInvokerTest.class);

    @Test
    void call() {
        String a = "wu://192.168.73.65:18000/com.github.wu.core.UserService?methods=hello,search,hi,hi";
        URL url = URL.of(a);
        Object[] args = new Object[1];
        args[0] = "world2";
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hello", args);
        RemoteInvoker<UserService> invoker = new RemoteInvoker<>(url, UserService.class);
        invoker.init();
        ApiResult apiResult = invoker.call(invocation);
        logger.info("apiResult: {}", apiResult);
    }
}