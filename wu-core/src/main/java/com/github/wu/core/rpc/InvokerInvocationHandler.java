package com.github.wu.core.rpc;

import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.core.transport.Invocations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author wangyongxu
 */
public class InvokerInvocationHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(InvokerInvocationHandler.class);

    private final Invoker<?> invoker;

    public InvokerInvocationHandler(Invoker<?> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            if ("toString".equals(methodName)) {
                return invoker.toString();
            } else if ("hashCode".equals(methodName)) {
                return invoker.hashCode();
            }
        } else if (parameterTypes.length == 1 && "equals".equals(methodName)) {
            return invoker.equals(args[0]);
        }
        Invocation invocation = Invocations.parseInvocation(method, args);
        ApiResult apiResult = invoker.call(invocation);
        return apiResult.recreate();
    }
}
