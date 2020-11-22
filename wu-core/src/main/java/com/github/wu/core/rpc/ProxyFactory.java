package com.github.wu.core.rpc;

/**
 * @author wangyongxu
 */
public interface ProxyFactory {

    <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces);
}
