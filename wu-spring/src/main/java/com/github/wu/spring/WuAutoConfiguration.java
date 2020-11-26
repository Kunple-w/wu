package com.github.wu.spring;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangyongxu
 */
@Configuration
@EnableConfigurationProperties(WuConfigurationProperties.class)
public class WuAutoConfiguration {
}
