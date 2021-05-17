package com.github.wu.core.register;

import com.github.wu.common.URL;
import com.github.wu.core.UserService;
import com.github.wu.core.UserServiceImpl;
import com.github.wu.core.rpc.exception.ServiceNoSuchMethodException;
import com.github.wu.core.rpc.remoting.filter.FilterRegistry;
import com.github.wu.core.rpc.remoting.transport.ApiResult;
import com.github.wu.core.rpc.remoting.transport.Invocation;
import com.github.wu.core.rpc.remoting.transport.Invocations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class DefaultProviderTest {
    private static final Logger logger = LoggerFactory.getLogger(DefaultProviderTest.class);

    @Test
    void getImpl() {
        String a = "http://baidu.com";
        URL url = URL.of(a);
        UserServiceImpl userService = new UserServiceImpl();
        DefaultProvider<UserService> provider = new DefaultProvider<>(url, UserService.class, userService, new FilterRegistry().getFilterChain());
        UserService impl = provider.getImpl();

        Assertions.assertSame(userService, impl, "对象不一致");
    }

    @Test
    void call() {
        String a = "http://baidu.com";
        URL url = URL.of(a);
        UserServiceImpl userService = new UserServiceImpl();
        DefaultProvider<UserService> provider = new DefaultProvider<>(url, UserService.class, userService, new FilterRegistry().getFilterChain());
        Object[] args = new Object[1];
        args[0] = "world";
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hello", args);
        ApiResult apiResult = provider.call(invocation);
        Assertions.assertEquals(ApiResult.success("hello world"), apiResult, "api result");
    }

    @Test
    void callMethodNotExisted() {
        String a = "http://baidu.com";
        URL url = URL.of(a);
        UserServiceImpl userService = new UserServiceImpl();
        DefaultProvider<UserService> provider = new DefaultProvider<>(url, UserService.class, userService, new FilterRegistry().getFilterChain());
        Object[] args = new Object[1];
        args[0] = "world";
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hello2", args);
        ApiResult apiResult = provider.call(invocation);
        ServiceNoSuchMethodException exception = new ServiceNoSuchMethodException("Invocation(serviceName=com.github.wu.core.UserService, methodName=hello2, args=[world]) not existed");
        Assertions.assertEquals(ApiResult.exception(exception).getThrowable().getMessage(), apiResult.getThrowable().getMessage(), "api result");
    }

    @Test
    void callBizError() {
        String a = "http://baidu.com";
        URL url = URL.of(a);
        UserServiceImpl userService = new UserServiceImpl();
        DefaultProvider<UserService> provider = new DefaultProvider<>(url, UserService.class, userService, new FilterRegistry().getFilterChain());
        Object[] args = new Object[2];
        args[0] = null;
        args[1] = "can you help me ?";
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hi", args);
        ApiResult apiResult = provider.call(invocation);
        IllegalArgumentException exception = new IllegalArgumentException("name is empty");
        Assertions.assertEquals(ApiResult.exception(exception).getThrowable().getMessage(), apiResult.getThrowable().getMessage(), "api result");
    }

    @Test
    void callBizError2() {
        String a = "http://baidu.com";
        URL url = URL.of(a);
        UserServiceImpl userService = new UserServiceImpl();
        DefaultProvider<UserService> provider = new DefaultProvider<>(url, UserService.class, userService);
        Object[] args = new Object[3];
        args[0] = "wu";
        args[1] = "can you help me ?";
        args[2] = 2;
        Invocation invocation = Invocations.parseInvocation(UserService.class, "hi", args);
        ApiResult apiResult = provider.call(invocation);
        Assertions.assertEquals(ApiResult.success(null), apiResult, "api result");
    }

    @Test
    void callBizError3() throws Exception {
        String a = "http://baidu.com";
        URL url = URL.of(a);
        UserServiceImpl userService = new UserServiceImpl();
        DefaultProvider<UserService> provider = new DefaultProvider<>(url, UserService.class, userService);
        Object[] args = new Object[2];
        args[0] = "wu";
        args[1] = 2;
        Invocation invocation = Invocations.parseInvocation(UserService.class, "search", args);
        ApiResult apiResult = provider.call(invocation);
        List<String> msgList = userService.search("wu", 2);
        Assertions.assertEquals(msgList.size(), ((List<String>) apiResult.getValue()).size(), "api result");
    }

    @Test
    void callBizError4() {
        String a = "http://baidu.com";
        URL url = URL.of(a);
        UserServiceImpl userService = new UserServiceImpl();
        DefaultProvider<UserService> provider = new DefaultProvider<>(url, UserService.class, userService);
        Object[] args = new Object[2];
        args[0] = "";
        args[1] = 2;
        Invocation invocation = Invocations.parseInvocation(UserService.class, "search", args);
        ApiResult apiResult = provider.call(invocation);
        Assertions.assertEquals("入参有误", apiResult.getThrowable().getMessage());
    }
}