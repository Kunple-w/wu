package com.github.wu.core.config;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.Exporter;
import com.github.wu.core.rpc.remoting.filter.FilterRegistry;
import com.github.wu.core.rpc.remoting.transport.EndPointFactory;
import com.github.wu.core.rpc.remoting.transport.EndPointFactoryImpl;
import com.github.wu.core.rpc.remoting.transport.Server;
import com.github.wu.registry.api.RegisterService;

/**
 * @author wangyongxu
 */
public class ExportConfig<T> extends AbstractRegistryHandler {

    /**
     * interface class
     */
    private Class<T> interfaceClazz;

    private T impl;
    /**
     * provider url
     */
    protected URL providerUrl;

    private Exporter<T> exporter;

    private ServiceConfig serviceConfig;

    private FilterRegistry filterRegistry;


    private final EndPointFactory endPointFactory = new EndPointFactoryImpl();

    public ExportConfig(Class<T> interfaceClazz, T impl, RegistryConfig registryConfig, ServiceConfig serviceConfig, FilterRegistry filterRegistry) {
        super(registryConfig);
        this.interfaceClazz = interfaceClazz;
        this.impl = impl;
        this.serviceConfig = serviceConfig;
        this.filterRegistry = filterRegistry;
    }

    public ExportConfig(RegistryConfig registryConfig) {
        super(registryConfig);
    }

    protected void initExporter() {
        exporter = new Exporter<>(interfaceClazz, impl, filterRegistry);
        exporter.setProtocol(serviceConfig.getProtocol());
        exporter.setPort(serviceConfig.getPort());
        exporter.setServer(getServer());
        exporter.export();

        providerUrl = exporter.getURL();
    }

    private Server getServer() {
        return endPointFactory.createServer(serviceConfig.getPort());
    }

    public void export() {
        initRegistry();
        initExporter();
        exportToRegistry(providerUrl);
    }

    protected void exportToRegistry(URL url) {
        for (RegisterService registerService : registerServices) {
            registerService.register(url);
        }
    }

}
