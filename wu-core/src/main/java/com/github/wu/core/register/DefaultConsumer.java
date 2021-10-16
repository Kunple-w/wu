package com.github.wu.core.register;

import com.github.wu.common.URL;
import com.github.wu.common.exception.RpcException;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.remoting.filter.FilterChain;
import com.github.wu.core.rpc.remoting.filter.FilterRegistry;
import com.github.wu.core.rpc.remoting.filter.WuFilter;
import com.github.wu.core.rpc.remoting.transport.ApiResult;
import com.github.wu.core.rpc.remoting.transport.Invocation;

import java.util.List;

/**
 * @author wangyongxu
 */
public class DefaultConsumer<T> implements Consumer<T> {

    private final Object lock = new Object();

    private final URL url;

    private final Class<T> interfaceClass;

    private final Invoker<T> invoker;

    private FilterRegistry filterRegistry;


    public DefaultConsumer(URL url, Class<T> interfaceClass, Invoker<T> invoker, FilterRegistry filterRegistry) {
        this.url = url;
        this.interfaceClass = interfaceClass;
        this.invoker = invoker;
        this.filterRegistry = filterRegistry;
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public ApiResult call(Invocation invocation) {
        return call(invocation, invoker);
    }

    protected ApiResult call(Invocation invocation, Invoker<?> invoker) {
        ApiResult result = ApiResult.empty();
        FilterChain filterChain = getFilterChain();
        try {
            boolean before = filterChain.applyBefore(invocation, result);
            if (!before) {
                return result;
            }
            result = invoker.call(invocation);
            filterChain.applyAfter(invocation, result);
        } catch (RpcException e) {
            result.setThrowable(e);
            filterChain.applyComplete(invocation, result, e);
        }
        return result;
    }

    protected FilterChain getFilterChain() {
        List<WuFilter> interceptors = filterRegistry.getInterceptors();
        WuFilter[] array = interceptors.toArray(new WuFilter[0]);
        return new FilterChain(array);
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return invoker.isAvailable();
    }

    @Override
    public void init() {
        invoker.init();
    }

    @Override
    public void destroy() {
        invoker.destroy();
    }
}
