package com.github.wu.registry.api;

import com.github.wu.common.Node;
import com.github.wu.common.URL;

import java.util.List;

/**
 * register service
 *
 * @author wangyongxu
 */
public interface RegisterService extends Node {


    /**
     * unregister service
     *
     * @param url :
     * @author wangyongxu
     */
    void register(URL url);

    /**
     * unregister service
     *
     * @param url :
     * @author wangyongxu
     */
    void unregister(URL url);

    /**
     * lookup url for urls
     *
     * @param url : url
     * @return java.util.List<com.github.wu.common.URL>
     * @author wangyongxu
     */
    List<URL> lookup(URL url);

    /**
     * subscribe service
     *
     * @param url           : url
     * @param eventListener : eventListener
     * @author wangyongxu
     */
    void subscribe(URL url, EventListener eventListener);

    /**
     * unsubscribe service
     *
     * @param url           : serviceName
     * @param eventListener : eventListener
     * @author wangyongxu
     */
    void unsubscribe(URL url, EventListener eventListener);

}
