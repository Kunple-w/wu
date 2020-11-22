package com.github.wu.core.rpc.exception;


import com.github.wu.common.exception.WuRuntimeException;

/**
 * @author wangyongxu
 */
public class ServiceNotRegisterException extends WuRuntimeException {
    public ServiceNotRegisterException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServiceNotRegisterException(String message) {
        super(message);
    }
}
