package com.github.wu.registry.api;

import com.github.wu.common.URL;
import com.github.wu.common.exception.WuRuntimeException;
import com.github.wu.registry.api.exception.RegistryCreatedFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyongxu
 */
public abstract class AbstractRegistryFactory implements com.github.wu.registry.api.RegistryFactory {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<URL, com.github.wu.registry.api.RegisterService> cache = new HashMap<>();

    @Override
    public synchronized com.github.wu.registry.api.RegisterService getRegistry(URL url) {
        com.github.wu.registry.api.RegisterService registry = cache.get(url);
        if (registry == null) {
            try {
                registry = createRegistry(url);
                cache.put(url, registry);
            } catch (RegistryCreatedFailed e) {
                throw new WuRuntimeException(e);
            }
        }
        return registry;
    }

    /**
     * create registry
     *
     * @param url : url
     * @return com.github.wu.registry.api.RegisterService
     * @throws RegistryCreatedFailed, if create failed
     * @author wangyongxu
     */
    public abstract com.github.wu.registry.api.RegisterService createRegistry(URL url) throws RegistryCreatedFailed;
}
