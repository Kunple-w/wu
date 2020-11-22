package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.LoadBalance;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;

import java.util.List;

/**
 * @author wangyongxu
 */
public class FailFastClusterInvoker<T> extends AbstractClusterInvoker<T> {
    public FailFastClusterInvoker(URL registryUrl, Class<T> interfaceClass) {
        this.registryUrl = registryUrl;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public ApiResult call(Invocation invocation, List<Invoker<T>> invokers, LoadBalance<T> loadBalance) {
        Invoker invoker = select(loadBalance, invokers, invocation);
        return invoker.call(invocation);
    }
}
