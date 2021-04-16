package com.github.wu.core.rpc.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyongxu
 */
public class FilterRegistry {
    private List<WuFilter> interceptors = new ArrayList<>();
    private Map<String, WuFilter> interceptorMap = new HashMap<>();

    public void add(WuFilter interceptor) {
        interceptors.add(interceptor);
    }

    public List<WuFilter> getInterceptors() {

        return interceptors;
    }
}
