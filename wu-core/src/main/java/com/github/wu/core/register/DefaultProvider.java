package com.github.wu.core.register;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.exception.ServiceNoSuchMethodException;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wangyongxu
 */
public class DefaultProvider<T> extends AbstractProvider<T> {

    protected final T impl;

    /**
     * constructor
     *
     * @param url  : only param context
     * @param cls  : interface
     * @param impl : impl class
     * @author wangyongxu
     */
    public DefaultProvider(URL url, Class<T> cls, T impl) {
        super(url, cls);
        this.impl = impl;
    }

    @Override
    public T getImpl() {
        return impl;
    }

    @Override
    public ApiResult call(Invocation invocation) {
        Method method = lookupMethod(invocation.getMethodName(), invocation.getArgsDesc());
        if (method == null) {
            return ApiResult.exception(new ServiceNoSuchMethodException(invocation.toString() + " not existed"));
        }
        try {
            // TODO: 2021-05-08 06:05:12 server filter使用代理生成 by wangyongxu
            Object result = method.invoke(impl, invocation.getArgs());
            return ApiResult.success(result);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                return ApiResult.exception(e.getTargetException());
            }
            return ApiResult.exception(e);
        } catch (Exception e) {
            return ApiResult.exception(e);
        }
    }
}
