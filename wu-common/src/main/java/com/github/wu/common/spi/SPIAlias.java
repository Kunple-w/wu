package com.github.wu.common.spi;

import java.lang.annotation.*;

/**
 * service provider name alias
 *
 * @author wangyongxu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPIAlias {


    /**
     * provider name alias
     * if name is empty, will use nameGenerator
     */
    String alias() default "";

    /**
     * name generator
     */
    // TODO: 2020-11-04 04:07:27 nameGenerator cache by wangyongxu
    Class<? extends NameGenerator> nameGenerator() default DefaultNameGenerator.class;
}
