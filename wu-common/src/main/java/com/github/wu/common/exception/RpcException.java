package com.github.wu.common.exception;

/**
 * @author wangyongxu
 */
public class RpcException extends WuRuntimeException {
    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
