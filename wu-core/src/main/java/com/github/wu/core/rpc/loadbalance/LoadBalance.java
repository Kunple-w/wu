package com.github.wu.core.rpc.loadbalance;

import com.github.wu.common.URL;
import com.github.wu.common.spi.SPI;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.remoting.transport.Invocation;

import java.util.List;

/**
 * load balance
 *
 * @author qiankewei
 */

@SPI
public interface LoadBalance {

    <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation);


}
