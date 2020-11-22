package com.github.wu.core.rpc;

import com.github.wu.core.transport.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangyongxu
 */
public class Reference<T> {
    private final Client client;
    private final Class<T> interfaceClass;
    private T stub;

    public Reference(Client client, Class<T> interfaceClass) {
        this.client = client;
        this.interfaceClass = interfaceClass;
    }

    @SuppressWarnings("unchecked")
    public synchronized T refer() {
        if (stub == null) {
            stub = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new RemoteInvocationHandler(client));
        }
        return stub;
    }

    private static class RemoteInvocationHandler implements InvocationHandler {

        private final Client client;

        public RemoteInvocationHandler(Client client) {
            this.client = client;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Invocation invocation = Invocations.parseInvocation(method, args);
            Request request = new Request(invocation);
            Object body = client.send(request).getBody();

            if (body instanceof ApiResult) {
                return handleApiResult((ApiResult) body);
            }
            return body;
        }

        private Object handleApiResult(ApiResult apiResult) throws Throwable {
            if (apiResult.isSuccess()) {
                return apiResult.getValue();
            }
            throw apiResult.getThrowable();
        }
    }

}
