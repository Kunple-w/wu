package com.github.wu.spring;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;


/**
 * Custom scanning for interfaces extending the given base interface, include interfaces annotated with {@link WuService}.
 * @author qiankewei
 */
public class WuComponentProvider extends ClassPathScanningCandidateComponentProvider {

    public WuComponentProvider(Iterable<? extends TypeFilter> includeFilters) {
        super(false);
        Assert.notNull(includeFilters, "Include filters must not be null");

        if (includeFilters.iterator().hasNext()) {
            for (TypeFilter filter : includeFilters) {
                addIncludeFilter(filter);
            }
        }
    }

}
