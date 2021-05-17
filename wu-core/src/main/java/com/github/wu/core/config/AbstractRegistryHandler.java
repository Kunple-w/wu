package com.github.wu.core.config;

import com.github.wu.common.URL;
import com.github.wu.common.URLConstant;
import com.github.wu.common.spi.ExtensionLoader;
import com.github.wu.registry.api.RegisterService;
import com.github.wu.registry.api.RegistryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangyongxu
 */
public abstract class AbstractRegistryHandler {
    /**
     * registry config list
     */
    protected List<RegistryConfig> registryConfigs = new ArrayList<>();

    protected List<URL> registryUrls = new ArrayList<>();

    protected List<RegisterService> registerServices = new ArrayList<>();
    private final Object lock = new Object();

    /**
     * init flag
     */
    protected AtomicBoolean init = new AtomicBoolean(false);

    public AbstractRegistryHandler(List<RegistryConfig> registryConfigs) {
        this.registryConfigs = registryConfigs;
    }

    public AbstractRegistryHandler(RegistryConfig registryConfig) {
        this.registryConfigs.add(registryConfig);
    }

    public AbstractRegistryHandler() {
    }

    protected void loadRegistryURL() {
        for (RegistryConfig registryConfig : registryConfigs) {
            URL url = new URL(registryConfig.getProtocol(), registryConfig.getHost(), registryConfig.getPort(), "");
            url.setParam(URLConstant.TIMEOUT_KEY, String.valueOf(registryConfig.getTimeout()));
            registryUrls.add(url);
        }
    }

    protected void loadRegistryList() {
        ExtensionLoader<RegistryFactory> extensionLoader = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
        for (URL registryUrl : registryUrls) {
            registerServices.add(extensionLoader.getExtension(registryUrl.getProtocol()).getRegistry(registryUrl));
        }
    }

    public synchronized void initRegistry() {
        if (init.get()) {
            return;
        }
        try {
            loadRegistryURL();
            loadRegistryList();
            init.set(true);
        } catch (Exception e) {
            init.set(false);
        }

    }

}
