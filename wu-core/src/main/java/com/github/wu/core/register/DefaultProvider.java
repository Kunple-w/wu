package com.github.wu.core.register;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.exception.ServiceNoSuchMethodException;
import com.github.wu.core.rpc.remoting.filter.FilterChain;
import com.github.wu.core.rpc.remoting.transport.ApiResult;
import com.github.wu.core.rpc.remoting.transport.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wangyongxu
 */
public class DefaultProvider<T> extends AbstractProvider<T> {

    protected final T impl;

    private FilterChain filterChain;

    /**
     * constructor
     *
     * @param url  : only param context
     * @param cls  : interface
     * @param impl : impl class
     * @author wangyongxu
     */
    public DefaultProvider(URL url, Class<T> cls, T impl, FilterChain filterChain) {
        super(url, cls);
        this.impl = impl;
        this.filterChain = filterChain;
    }

    public DefaultProvider(URL url, Class<T> cls, T impl) {
        this(url, cls, impl, FilterChain.EMPTY);
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

        ApiResult result = ApiResult.empty();
        try {
            boolean before = filterChain.applyBefore(invocation, result);
            if (!before) {
                return result;
            }
            result = call(method, impl, invocation.getArgs());
            filterChain.applyAfter(invocation, result);
        } catch (InvocationTargetException e) {
            result.resetException(e.getTargetException());
            filterChain.applyComplete(invocation, result, e.getTargetException());
        } catch (Exception e) {
            result.resetException(e);
            filterChain.applyComplete(invocation, result, e);
        }
        return result;
    }

    public ApiResult call(Method method, Object obj, Object... args) throws InvocationTargetException, IllegalAccessException {
        Object result = method.invoke(obj, args);
        return ApiResult.success(result);
    }

}
