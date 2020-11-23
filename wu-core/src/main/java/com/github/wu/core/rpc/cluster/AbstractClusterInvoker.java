package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.common.URLConstant;
import com.github.wu.common.spi.ExtensionLoader;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.loadbalance.LoadBalance;
import com.github.wu.core.rpc.RemoteInvoker;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.registry.api.RegisterService;
import com.github.wu.registry.api.RegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyongxu
 */
public abstract class AbstractClusterInvoker<T> implements ClusterInvoker<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * registry url, eg zookeeper://127.0.0.1:2181
     */
    protected URL registryUrl;

    protected Class<T> interfaceClass;

    private RegisterService registerService;


    @Override
    public URL getRegistryURL() {
        return registryUrl;
    }

    protected RegisterService loadRegistry() {
        ExtensionLoader<RegistryFactory> extensionLoader = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
        return extensionLoader.getExtension(registryUrl.getProtocol()).getRegistry(registryUrl);
    }

    @Override
    public synchronized RegisterService getRegistry() {
        if (registerService == null) {
            registerService = loadRegistry();
        }
        return registerService;
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public ApiResult call(Invocation invocation) {
        URL lookupURL = new URL(invocation.getServiceName());
        List<URL> urlList = registerService.lookup(lookupURL);
        List<Invoker<T>> invokers = providerList(urlList);
        LoadBalance loadBalance = initLoadBalance(invocation, invokers);
        return call(invocation, invokers, loadBalance);
    }

    protected List<Invoker<T>> providerList(List<URL> urlList) {
        List<Invoker<T>> invokers = new ArrayList<>();
        for (URL url : urlList) {
            RemoteInvoker<T> remoteInvoker = new RemoteInvoker<>(url, interfaceClass);
            invokers.add(remoteInvoker);
        }
        return invokers;
    }

    protected LoadBalance initLoadBalance(Invocation invocation, List<Invoker<T>> invokers) {
        ExtensionLoader<LoadBalance> extensionLoader = ExtensionLoader.getExtensionLoader(LoadBalance.class);
        return extensionLoader.getExtension(invokers.get(0).getURL().getParam(URLConstant.LOAD_BALANCE_KEY, URLConstant.LOAD_BALANCE_RANDOM));
    }

    protected <B> Invoker<B> select(LoadBalance loadBalance, List<Invoker<B>> invokers, Invocation invocation) {
        if (invokers.isEmpty()) {
            return null;
        }
        return loadBalance.select(invokers, getURL(), invocation);
    }

    public abstract ApiResult call(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadBalance);

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return registerService.isAvailable();
    }

    @Override
    public void init() {
        registerService = loadRegistry();
        registerService.init();
    }

    @Override
    public void destroy() {
        registerService.destroy();
    }
}
