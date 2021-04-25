package com.github.wu.core.transport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author wangyongxu
 */
@Data
public class ApiResult {

    private static final int SUCCESS = 0;
    private static final int FAILED = -1;
    private static final int EMPTY = -2;

    private Object value;

    private int code;

    private Throwable throwable;

    public ApiResult(Object value, int code, Throwable throwable) {
        this.value = value;
        this.code = code;
        this.throwable = throwable;
    }

    public ApiResult() {
    }

    public static ApiResult success(Object value) {
        return new ApiResult(value, SUCCESS, null);
    }

    public static ApiResult empty() {
        return new ApiResult(null, EMPTY, null);
    }

    public static ApiResult success() {
        return new ApiResult(null, SUCCESS, null);
    }

    public static ApiResult exception(Throwable throwable) {
        return new ApiResult(null, FAILED, throwable);
    }

    public Object recreate() throws Throwable {
        if (isSuccess()) {
            return value;
        } else {
            throw throwable;
        }
    }

    @JsonIgnore
    public boolean isSuccess() {
        return code == SUCCESS;
    }

    @Override
    public String toString() {
        return "ApiResult=[value=" + value.toString() + ", code=" + code + ", throwable=" + throwable.getMessage() + "]";
    }
}
