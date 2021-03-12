package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.common.exception.WuRuntimeException;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.RpcContext;
import com.github.wu.core.rpc.exception.ServiceUnavailableException;
import com.github.wu.core.rpc.loadbalance.LoadBalance;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;

import java.util.List;

/**
 * @author wangyongxu
 */
public class FailoverClusterInvoker<T> extends AbstractClusterInvoker<T> {

    public FailoverClusterInvoker(URL registryUrl, Class<T> interfaceClass) {
        this.registryUrl = registryUrl;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public ApiResult call(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadBalance) {
        return failover(invocation, invokers, loadBalance);
    }

    private ApiResult failover(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadBalance) {
        if (invokers == null || invokers.isEmpty()) {
            throw new ServiceUnavailableException("service " + invocation.getServiceName() + " unavailable");
        }

        Invoker<T> invoker = select(loadBalance, invokers, invocation);
        try {
            ApiResult apiResult = invoker.call(invocation);
            if (shouldFailover(apiResult)) {
                invokers.remove(invoker);
                return failover(invocation, invokers, loadBalance);
            }
            RpcContext.get().getResponse().put(invoker.getURL(), apiResult);
            return apiResult;
        } catch (WuRuntimeException e) {
            logger.error("invoker call exception, try failover", e);
            invokers.remove(invoker);
            return failover(invocation, invokers, loadBalance);
        }

    }

    private boolean shouldFailover(ApiResult apiResult) {
        if (apiResult == null) {
            return true;
        }
        return !apiResult.isSuccess() && apiResult.getThrowable() instanceof WuRuntimeException;
    }

}
