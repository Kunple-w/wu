package com.github.wu.core.rpc.loadbalancestrategy;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.LoadBalance;
import com.github.wu.core.rpc.Reference;

import java.util.List;

/**
 * @author qiankewei
 */
public class RoundRobinLoadBalance<T> implements LoadBalance<T> {
    @Override
    public Reference<T> select(List<Reference<T>> references, URL url) {
        return null;
    }
}
