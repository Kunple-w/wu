package com.github.wu.core.rpc.remoting.filter;

import com.github.wu.common.exception.RpcException;
import com.github.wu.core.rpc.remoting.transport.ApiResult;
import com.github.wu.core.rpc.remoting.transport.Invocation;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor chain
 *
 * @author wangyongxu
 */
public class FilterChain {
    private static final Logger logger = LoggerFactory.getLogger(FilterChain.class);

    public static final FilterChain EMPTY = new FilterChain();

    private WuFilter[] filterArray;

    /**
     * 执行到当前filter的索引
     */
    private int filterIndex;

    public FilterChain(WuFilter[] filterArray) {
        this.filterArray = filterArray;
    }

    public FilterChain() {
        this(new WuFilter[0]);
    }

    private WuFilter[] getFilterArray() {
        return filterArray;
    }

    public boolean applyBefore(Invocation invocation, ApiResult apiResult) throws RpcException {
        WuFilter[] filters = getFilterArray();
        if (ObjectUtils.isNotEmpty(filters)) {
            for (int i = 0; i < filters.length; i++) {
                WuFilter filter = filters[i];
                if (!filter.before(invocation, apiResult)) {
                    applyComplete(invocation, apiResult, null);
                    return false;
                }
                filterIndex = i;
            }
        }
        return true;
    }

    /**
     * 在方法执行完成后回调
     * <p>
     * 和before方法的调用顺序相反
     *
     * @param invocation : invocation
     * @param apiResult  : 结果
     * @author wangyongxu
     */
    public void applyAfter(Invocation invocation, ApiResult apiResult) throws RpcException {
        WuFilter[] filters = getFilterArray();
        if (ObjectUtils.isNotEmpty(filters)) {
            for (int i = filters.length - 1; i >= 0; i--) {
                WuFilter filter = filters[i];
                filter.after(invocation, apiResult);
            }
        }
    }

    /**
     * 回调complete方法
     *
     * <ul>
     * 以下两种情况下回执行此方法:
     * <li> 1. 该过滤器的before方法成功调用且返回true</li>
     * <li>2. 每个after方法调用完成后</li>
     * </ul>
     * <note>
     *     和before方法的调用顺序相反
     *     <p>
     *         实际上，如果拦截器执行了before方法且为true，那么一定回执行complete方法
     *     </p>
     * </note>
     *
     * @param invocation : 结果
     * @param apiResult  : 结果
     * @param ex         : 异常，如果after中没有抛出异常，则为null
     * @author wangyongxu
     */
    public void applyComplete(Invocation invocation, ApiResult apiResult, Throwable ex) {
        WuFilter[] filters = getFilterArray();
        if (ObjectUtils.isNotEmpty(filters)) {
            for (int i = filterIndex; i >= 0; i--) {
                WuFilter filter = filters[i];
                try {
                    filter.complete(invocation, apiResult, ex);
                } catch (Throwable throwable) {
                    logger.error("filter.complete throw exception. ", ex);
                }
            }
        }
    }
}
