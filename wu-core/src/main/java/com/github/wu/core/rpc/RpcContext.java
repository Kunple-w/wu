package com.github.wu.core.rpc;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.remoting.transport.ApiResult;
import com.github.wu.core.rpc.remoting.transport.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyongxu
 */
public class RpcContext {
    private static final InheritableThreadLocal<RpcContext> client = new InheritableThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    public static RpcContext get() {
        return client.get();
    }

    private Request request;

    private Map<URL, ApiResult> response = new HashMap<>();

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Map<URL, ApiResult> getResponse() {
        return response;
    }

    public void setResponse(Map<URL, ApiResult> response) {
        this.response = response;
    }
}
