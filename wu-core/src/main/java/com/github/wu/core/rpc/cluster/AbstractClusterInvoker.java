package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.common.URLConstant;
import com.github.wu.common.spi.ExtensionLoader;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.core.rpc.exception.ServiceNotRegisterException;
import com.github.wu.core.rpc.loadbalance.LoadBalance;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.registry.api.RegisterService;
import com.github.wu.registry.api.RegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Registry<T> registry;


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
        List<Invoker<T>> invokers = registry.lookup(invocation);
        if (invokers == null || invokers.isEmpty()) {
            throw new ServiceNotRegisterException("service " + invocation.getServiceName() + "not found");
        }
        LoadBalance loadBalance = initLoadBalance(invokers);
        return call(invocation, invokers, loadBalance);
    }

    protected LoadBalance initLoadBalance(List<Invoker<T>> invokers) {
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
        if (logger.isDebugEnabled()) {
            logger.debug("begin init cluster invoker.");
        }
        registerService = loadRegistry();
        registerService.init();
        initRegistry();
        if (logger.isDebugEnabled()) {
            logger.debug("init cluster invoker success.");
        }
    }

    private void initRegistry() {
        registry = new DefaultRegistry<>(registryUrl, interfaceClass);
        registry.init();
    }

    @Override
    public void destroy() {
        registerService.destroy();
        registry.destroy();
    }
}
