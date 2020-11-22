package com.github.wu.core.rpc;

import com.github.wu.core.transport.Request;

/**
 * @author wangyongxu
 */
public class RpcContext {
    private static InheritableThreadLocal<RpcContext> threadLocal= new InheritableThreadLocal<>();

    private Request request;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
    public static RpcContext getRpcContext(){
        return threadLocal.get();
    }
}
