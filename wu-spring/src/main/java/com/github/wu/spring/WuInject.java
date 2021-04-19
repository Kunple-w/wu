package com.github.wu.spring;

import java.lang.annotation.*;

/**
 * wu inject annotation
 *
 * @author wangyongxu
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface WuInject {

    /**
     * 如果本地的spring容器中存在时，不使用本地的bean
     *
     * @return boolean
     * @author wangyongxu
     */
    boolean usingLocal() default false;

}
