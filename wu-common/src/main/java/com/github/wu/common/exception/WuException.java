package com.github.wu.common.exception;

/**
 * Seng受检异常类
 *
 * @author wangyongxu
 */
public class WuException extends Exception {
    public WuException() {
    }

    public WuException(String message) {
        super(message);
    }

    public WuException(String message, Throwable cause) {
        super(message, cause);
    }

    public WuException(Throwable cause) {
        super(cause);
    }

    public WuException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
