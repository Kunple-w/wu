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

}
