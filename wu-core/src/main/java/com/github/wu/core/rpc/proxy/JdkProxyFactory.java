package com.github.wu.core.rpc.proxy;

import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.InvokerInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author wangyongxu
 */
public class JdkProxyFactory implements ProxyFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvokerInvocationHandler(invoker));
    }
}
