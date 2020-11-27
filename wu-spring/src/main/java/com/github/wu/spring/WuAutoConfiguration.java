package com.github.wu.spring;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author wangyongxu
 */
@EnableConfigurationProperties(WuConfigurationProperties.class)
public class WuAutoConfiguration {

    @Bean
    public WuServiceBeanPostProcessor wuServiceBeanPostProcessor(WuConfigurationProperties wuConfigurationProperties) {
        return new WuServiceBeanPostProcessor(wuConfigurationProperties);
    }

    @Bean
    public WuAutowiredAnnotationBeanPostProcessor wuAutowiredAnnotationBeanPostProcessor(WuConfigurationProperties wuConfigurationProperties) {
        return new WuAutowiredAnnotationBeanPostProcessor(wuConfigurationProperties);
    }
}
