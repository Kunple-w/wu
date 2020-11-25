package com.github.wu.spring;

import com.github.wu.common.utils.WuUtils;
import com.github.wu.core.rpc.config.RegistryConfig;
import com.github.wu.core.rpc.config.ServiceConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author wangyongxu
 */
@ConfigurationProperties(prefix = WuUtils.WU_PREFIX)
public class WuConfigurationProperties {

    @NestedConfigurationProperty
    private ServiceConfig service = new ServiceConfig();

    @NestedConfigurationProperty
    private RegistryConfig registry = new RegistryConfig();

    public ServiceConfig getService() {
        return service;
    }

    public void setService(ServiceConfig service) {
        this.service = service;
    }

    public RegistryConfig getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }
}
