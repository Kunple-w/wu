package com.github.wu.core.rpc;

import com.github.wu.common.Node;
import com.github.wu.common.URL;
import com.github.wu.common.URLConstant;
import com.github.wu.common.utils.NetUtils;
import com.github.wu.common.utils.ReflectUtils;
import com.github.wu.core.register.DefaultProvider;
import com.github.wu.core.register.Provider;
import com.github.wu.core.transport.Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * export service to server(remote)
 *
 * @author wangyongxu
 */
public class Exporter<T> implements Node {

    private AtomicBoolean available = new AtomicBoolean(false);

    public final Map<Class<T>, Provider<T>> providerMap = new ConcurrentHashMap<>();

    private String protocol;

    private int port;

    private Server server;

    protected Class<T> interfaceClazz;

    protected T impl;

    protected URL url;

    public Exporter(Class<T> interfaceClazz, T impl) {
        this.interfaceClazz = interfaceClazz;
        this.impl = impl;
    }

    public void export() {
        if (!available.get()) {
            init();
        }
    }

    @Override
    public void init() {
        url = buildUrl(interfaceClazz);
        DefaultProvider<T> provider = new DefaultProvider<>(url, interfaceClazz, impl);
        providerMap.put(interfaceClazz, provider);
        server.registerProvider(provider);
        available.set(true);
    }

    @Override
    public void destroy() {
        unExport(interfaceClazz);
    }


    public void unExport(Class<T> interfaceClazz) {
        Provider<T> provider = providerMap.get(interfaceClazz);
        if (provider != null) {
            server.unregisterProvider(provider);
        }
    }


    private String getHostToBind() {
        return NetUtils.getLocalHost();
    }

    private URL buildUrl(Class<T> interfaceClazz) {
        String localHost = getHostToBind();
        String className = ReflectUtils.getClassName(interfaceClazz);
        String methodNames = ReflectUtils.getClassMethodNames(interfaceClazz);
        URL url = new URL(protocol, localHost, port, className);
        url.getParameters().put(URLConstant.METHOD_KEY, methodNames);
        url.setPath(interfaceClazz.getName());
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return available.get();
    }
}
