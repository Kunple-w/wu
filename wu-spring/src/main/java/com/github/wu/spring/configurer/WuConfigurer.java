package com.github.wu.spring.configurer;

import com.github.wu.core.rpc.filter.FilterRegistry;

/**
 * @author wangyongxu
 */
public interface WuConfigurer {

    default void addFilter(FilterRegistry filterRegistry) {
    }

}
