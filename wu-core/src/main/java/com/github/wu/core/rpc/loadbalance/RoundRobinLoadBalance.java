package com.github.wu.core.rpc.loadbalance;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.transport.Invocation;

import java.util.List;

/**
 * @author qiankewei
 */
public class RoundRobinLoadBalance implements LoadBalance {
    private int current = 0;

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        Invoker<T> tInvoker = invokers.get(current);
        if (current >= invokers.size() - 1) {
            current = 0;
        } else {
            current++;
        }
        return tInvoker;
    }
}
