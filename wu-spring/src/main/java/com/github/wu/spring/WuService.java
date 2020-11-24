package com.github.wu.spring;

import java.lang.annotation.*;

/**
 * wu service annotation
 *
 * @author wangyongxu
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface WuService {

    Class<?> interfaceClass() default Void.class;
}
