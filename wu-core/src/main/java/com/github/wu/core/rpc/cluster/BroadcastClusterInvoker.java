package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.RpcContext;
import com.github.wu.core.rpc.loadbalance.LoadBalance;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;

import java.util.List;

/**
 * 广播，保证所有的节点都会被调用
 * 如果结果有异常，则返回最后一条异常，
 * 如果结果没有异常，则返回最后一个节点的结果
 *
 * @author wangyongxu
 */
public class BroadcastClusterInvoker<T> extends AbstractClusterInvoker<T> {
    public BroadcastClusterInvoker(URL registryUrl, Class<T> interfaceClass) {
        this.registryUrl = registryUrl;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public ApiResult call(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadBalance) {
        ApiResult failedResult = null;
        ApiResult ans = null;
        for (Invoker<T> invoker : invokers) {
            ans = invoker.call(invocation);
            logger.debug("node invoke finish, invoker {}, invocation: {}, lb: {}, result: {}", invoker, invocation, loadBalance, ans);
            RpcContext.get().getResponse().put(invoker.getURL(), ans);
            if (!ans.isSuccess()) {
                failedResult = ans;
                logger.warn("invoker invoke failed, invoker {}, invocation: {}, lb: {}, result: {}", invoker, invocation, loadBalance, ans);
            }
        }
        return failedResult != null ? failedResult : ans;
    }
}
