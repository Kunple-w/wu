package com.github.wu.spring;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @author wangyongxu
 */
@EnableConfigurationProperties(WuConfigurationProperties.class)
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = WuService.class))
@Configuration(proxyBeanMethods = false)
public class WuAutoConfiguration {

    @Bean
    public WuAutowiredAnnotationBeanPostProcessor wuAutowiredAnnotationBeanPostProcessor(WuConfigurationProperties wuConfigurationProperties) {
        return new WuAutowiredAnnotationBeanPostProcessor(wuConfigurationProperties);
    }

    @Bean
    public WuServiceListener wuServiceListener(WuConfigurationProperties wuConfigurationProperties) {
        return new WuServiceListener(wuConfigurationProperties);
    }
}
