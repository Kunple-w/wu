package com.github.wu.core.rpc.cluster;

import com.github.wu.common.Node;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.transport.Invocation;

import java.util.List;

/**
 * @author wangyongxu
 */
public interface Registry<T> extends Node {

    List<Invoker<T>> lookup(Invocation invocation);
}
