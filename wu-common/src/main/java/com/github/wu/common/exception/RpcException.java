package com.github.wu.common.exception;

/**
 * @author wangyongxu
 */
public class RpcException extends WuException {
    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
