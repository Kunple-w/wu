package com.github.wu.core.rpc.proxy;

import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.remoting.filter.FilterRegistry;

/**
 * @author wangyongxu
 */
public interface ProxyFactory {

    <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces, FilterRegistry filterRegistry);
}
