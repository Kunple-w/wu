package com.github.wu.core.register;

import com.github.wu.core.rpc.remoting.transport.ApiResult;
import com.github.wu.core.rpc.remoting.transport.Invocation;

/**
 * consumer
 *
 * @author wangyongxu
 */
public interface Callable {

    ApiResult call(Invocation invocation);
}
