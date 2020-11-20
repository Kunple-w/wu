package com.github.wu.common.spi;


import java.lang.annotation.*;

/**
 * spi
 *
 * @author wangyongxu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    Scope scope() default Scope.PROTOTYPE;
}
