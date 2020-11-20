package com.github.wu.common.exception;

/**
 * wu 运行时异常
 *
 * @author wangyongxu
 */
public class WuRuntimeException extends RuntimeException {

    public WuRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WuRuntimeException(String message) {
        super(message);
    }

    public WuRuntimeException(Throwable cause) {
        super(cause);
    }
}
