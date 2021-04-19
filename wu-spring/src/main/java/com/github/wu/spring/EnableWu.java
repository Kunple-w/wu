package com.github.wu.spring;

import com.github.wu.spring.configurer.ConfigurerSupport;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wangyongxu
 */

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = ConfigurerSupport.class)
public @interface EnableWu {
}
