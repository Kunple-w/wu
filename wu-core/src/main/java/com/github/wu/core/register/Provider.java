package com.github.wu.core.register;

import com.github.wu.common.Node;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * 提供者
 *
 * @author wangyongxu
 */
public interface Provider<T> extends Node, Callable {


    /**
     * get this provider interface
     *
     * @return java.lang.Class<T>
     * @author wangyongxu
     */
    Class<T> getInterface();

    /**
     * lookup method
     *
     * @param methodName : method name
     * @param paramDesc  : param desc
     * @return Java method
     * @author wangyongxu
     */
    @Nullable
    Method lookupMethod(String methodName, String paramDesc);

    /**
     * get impl
     *
     * @return object
     * @author wangyongxu
     */
    T getImpl();
}
