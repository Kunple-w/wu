package com.github.wu.core.rpc.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyongxu
 */
public class FilterRegistry {
    private List<WuFilter> interceptors = new ArrayList<>();

    public void add(WuFilter interceptor) {
        interceptors.add(interceptor);
    }

    public void setInterceptors(List<WuFilter> interceptors) {
        this.interceptors = interceptors;
    }

    public List<WuFilter> getInterceptors() {

        return interceptors;
    }
}
