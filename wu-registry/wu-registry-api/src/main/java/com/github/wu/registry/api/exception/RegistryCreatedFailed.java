package com.github.wu.registry.api.exception;

import com.github.wu.common.exception.WuException;

/**
 * @author wangyongxu
 */
public class RegistryCreatedFailed extends WuException {
    public RegistryCreatedFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistryCreatedFailed(String message) {
        super(message);
    }
}
