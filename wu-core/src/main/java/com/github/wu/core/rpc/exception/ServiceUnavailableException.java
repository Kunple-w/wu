package com.github.wu.core.rpc.exception;


import com.github.wu.common.exception.WuRuntimeException;

/**
 * @author wangyongxu
 */
public class ServiceUnavailableException extends WuRuntimeException {
    public ServiceUnavailableException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
