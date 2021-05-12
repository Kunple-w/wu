package com.github.wu.core.register;

import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;

/**
 * consumer
 *
 * @author wangyongxu
 */
public interface Callable {

    ApiResult call(Invocation invocation);
}
