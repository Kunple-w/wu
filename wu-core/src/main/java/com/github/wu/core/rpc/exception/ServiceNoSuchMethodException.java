package com.github.wu.core.rpc.exception;

/**
 * @author wangyongxu
 */
public class ServiceNoSuchMethodException extends ServiceNotRegisterException {
    public ServiceNoSuchMethodException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServiceNoSuchMethodException(String message) {
        super(message);
    }
}
