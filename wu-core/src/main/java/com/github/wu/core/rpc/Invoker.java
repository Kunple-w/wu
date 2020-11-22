package com.github.wu.core.rpc;

import com.github.wu.common.Node;
import com.github.wu.core.register.Callable;

/**
 * @author wangyongxu
 */
public interface Invoker<T> extends Node, Callable {
    /**
     * get this provider interface
     *
     * @return java.lang.Class<T>
     * @author wangyongxu
     */
    Class<T> getInterface();
}
