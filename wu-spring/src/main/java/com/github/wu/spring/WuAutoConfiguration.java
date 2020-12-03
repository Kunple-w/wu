package com.github.wu.spring;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author wangyongxu
 */
@EnableConfigurationProperties(WuConfigurationProperties.class)
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
