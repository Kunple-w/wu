package com.github.wu.core.rpc.loadbalance;

import com.github.wu.common.URL;
import com.github.wu.common.spi.SPIAlias;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.transport.Invocation;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * @author qiankewei
 */
@SPIAlias(alias = "random")
public class RandomLoadBalance implements LoadBalance {
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int i = RandomUtils.nextInt(0, invokers.size());
        return invokers.get(i);
    }
}
