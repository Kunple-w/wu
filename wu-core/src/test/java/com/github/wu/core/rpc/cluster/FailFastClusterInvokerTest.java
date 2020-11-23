package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.core.UserService;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.core.transport.Invocations;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FailFastClusterInvokerTest {
    private static final Logger logger = LoggerFactory.getLogger(FailFastClusterInvokerTest.class);

    @Test
    void call() {
        Object[] args = new Object[1];
        args[0] = "world";
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hello", args);

        URL registryUrl = URL.of("zookeeper://127.0.0.1:2181");
        FailFastClusterInvoker<UserService> failFastClusterInvoker = new FailFastClusterInvoker<>(registryUrl, UserService.class);
        failFastClusterInvoker.init();
        ApiResult apiResult = failFastClusterInvoker.call(invocation);
        logger.info("result: {}", apiResult);
    }
}