package com.github.wu.core.rpc.filter;

import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;

/**
 * rpc interceptor
 *
 * @author wangyongxu
 */
public interface WuFilter {

    /**
     * pre handle
     * <p>
     * 调用之前执行该方法
     *
     * @param invocation : invocation
     * @param apiResult  : 结果
     * @return {@link ApiResult}
     * @author wangyongxu
     */
    default boolean before(Invocation invocation, ApiResult apiResult) throws Exception {
        return true;
    }

    /**
     * after
     * <p>
     * 正常调用完成后
     * 如果正常调用失败，不执行该方法
     *
     * @param invocation : invocation
     * @param apiResult  : 结果
     * @author wangyongxu
     */
    default void after(Invocation invocation, ApiResult apiResult) throws Exception {

    }

    /**
     * afterComplete
     * <p>
     * before=false时或者调用完成后一定执行
     *
     * @param invocation : invocation
     * @param apiResult  : 结果
     * @param ex         : 异常，如果没有异常，则为空
     * @author wangyongxu
     */
    default void complete(Invocation invocation, ApiResult apiResult, Exception ex) {

    }

}
