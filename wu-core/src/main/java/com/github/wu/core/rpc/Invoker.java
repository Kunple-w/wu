package com.github.wu.core.rpc;

import com.github.wu.common.Node;
import com.github.wu.core.rpc.remoting.transport.ApiResult;
import com.github.wu.core.rpc.remoting.transport.Invocation;

/**
 * @author wangyongxu
 */
public interface Invoker<T> extends Node {
    /**
     * get this provider interface
     *
     * @return java.lang.Class<T>
     * @author wangyongxu
     */
    Class<T> getInterface();

    ApiResult call(Invocation invocation);

}
