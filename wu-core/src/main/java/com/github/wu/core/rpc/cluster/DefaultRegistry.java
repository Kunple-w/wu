package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.common.spi.ExtensionLoader;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.RemoteInvoker;
import com.github.wu.core.transport.Invocation;
import com.github.wu.registry.api.RegisterService;
import com.github.wu.registry.api.RegistryFactory;
import com.github.wu.registry.api.UrlListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wangyongxu
 */
public class DefaultRegistry<T> implements Registry<T>, UrlListener {

    private List<URL> availableUrl = new ArrayList<>();

    private List<Invoker<T>> availableInvokers = new ArrayList<>();

    private URL registryUrl;

    private RegisterService registerService;

    private Class<T> interfaceClass;

    private URL lookUrl;

    public DefaultRegistry(URL registryUrl, Class<T> interfaceClass) {
        this.registryUrl = registryUrl;
        this.interfaceClass = interfaceClass;
        this.lookUrl = new URL(interfaceClass.getName());
    }

    @Override
    public List<Invoker<T>> lookup(Invocation invocation) {
        if (availableInvokers.isEmpty()) {
            availableInvokers = lookup(lookUrl);
        }
        return availableInvokers;
    }

    private List<Invoker<T>> lookup(URL lookupURL) {
        List<URL> lookup = registerService.lookup(lookupURL);
        return initInvokers(lookup, interfaceClass);
    }

    protected List<Invoker<T>> initInvokers(List<URL> urlList, Class<T> interfaceClass) {
        List<Invoker<T>> invokers = new ArrayList<>();
        for (URL url : urlList) {
            Invoker<T> remoteInvoker = new RemoteInvoker<>(url, interfaceClass);
            invokers.add(remoteInvoker);
        }
        return invokers;
    }

    @Override
    public URL getURL() {
        return registryUrl;
    }

    @Override
    public boolean isAvailable() {
        return registerService.isAvailable();
    }

    @Override
    public void init() {
        if (registerService == null) {
            registerService = loadRegistry();
            registerService.subscribe(lookUrl, this);
        }
    }

    protected RegisterService loadRegistry() {
        ExtensionLoader<RegistryFactory> extensionLoader = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
        return extensionLoader.getExtension(registryUrl.getProtocol()).getRegistry(registryUrl);
    }

    @Override
    public void destroy() {
        registerService.destroy();
    }

    @Override
    public void onEvent(URLChanged context) {
        Set<URL> nowUrls = context.getNow();
        availableUrl = new ArrayList<>(nowUrls);
        availableInvokers = initInvokers(availableUrl, interfaceClass);

    }
}
