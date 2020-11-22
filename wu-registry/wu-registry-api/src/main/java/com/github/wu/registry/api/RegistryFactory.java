package com.github.wu.registry.api;

import com.github.wu.common.URL;
import com.github.wu.common.spi.SPI;
import com.github.wu.common.spi.Scope;

/**
 * registry factory
 *
 * @author wangyongxu
 */
@SPI(scope = Scope.SINGLETON)
public interface RegistryFactory {

    /**
     * get registry
     *
     * @param url : url
     * @return com.github.wu.registry.api.RegisterService
     * @author wangyongxu
     */
    com.github.wu.registry.api.RegisterService getRegistry(URL url);
}
