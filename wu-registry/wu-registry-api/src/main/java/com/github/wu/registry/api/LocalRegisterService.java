package com.github.wu.registry.api;

import com.github.wu.common.URL;
import com.github.wu.common.URLConstant;
import com.github.wu.common.exception.WuRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangyongxu
 */
public class LocalRegisterService implements com.github.wu.registry.api.RegisterService {

    // TODO: 2020-10-30 09:39:03 线程池配置 by wangyongxu
    ExecutorService executorService = Executors.newFixedThreadPool(1);

    // TODO: 这里的key是serviceName还是具体的url，下面不统一。
    protected Map<String, Set<URL>> cache = new HashMap<>();

    protected Map<URL, Set<com.github.wu.registry.api.EventListener>> listenerMap = new HashMap<>();

    @Override
    public void register(URL url) {
        checkUrl(url);
        String serviceKey = getServiceKey(url);
        cache.compute(serviceKey, (k, v) -> {
            if (v == null) {
                Set<URL> newSet = new HashSet<>();
                Set<URL> old = new HashSet<>(newSet);
                newSet.add(url);
                serviceChanged(new URLChanged(new HashSet<>(newSet)), url);
                return newSet;
            } else {
                Set<URL> old = new HashSet<>(v);
                v.add(url);
                serviceChanged(new URLChanged(new HashSet<>(v)), url);
                return v;
            }
        });
    }

    protected String getServiceKey(URL url) {
        return url.getParam(URLConstant.APPLICATION_KEY);
    }

    private void checkUrl(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url can't be null");
        }
    }

    @Override
    public void unregister(URL url) {
        checkUrl(url);
        String serviceKey = getServiceKey(url);
        cache.computeIfPresent(serviceKey, (k, v) -> {
            Set<URL> old = new HashSet<>(v);
            v.remove(url);
            serviceChanged(new URLChanged(new HashSet<>(v)), url);
            return v;
        });
    }

    @Override
    public List<URL> lookup(URL url) {
        return new ArrayList<>(cache.getOrDefault(getServiceKey(url), Collections.emptySet()));
    }

    private class NotifyWorker implements Runnable {
        private final URLChanged changed;
        private URL url;

        public NotifyWorker(URLChanged changed, URL url) {
            this.changed = changed;
            this.url = url;
        }

        @Override
        public void run() {
            // TODO: 这里绑定的监听器是针对某个URL的，为什么要把所有seviceKey对应的URL都传过去，是不是这里监听器的key也需要改变一下
            Set<com.github.wu.registry.api.EventListener> listeners = listenerMap.get(url);
            if (listeners != null && !listeners.isEmpty()) {
                for (com.github.wu.registry.api.EventListener listener : listeners) {
                    listener.onEvent(changed);
                }
            }
        }
    }

    protected void serviceChanged(URLChanged urlChanged, URL url) {
        executorService.execute(new NotifyWorker(urlChanged, url));
    }

    @Data
    @AllArgsConstructor
    public static class URLChanged {
        //        private String serviceKey;
//        private Set<URL> old;
        private Set<URL> now;

    }

    @Override
    public void subscribe(URL url, com.github.wu.registry.api.EventListener eventListener) {
        if (url == null) {
            throw new IllegalArgumentException("serviceName is null");
        }
        List<URL> urls = lookup(url);
        if (urls.isEmpty()) {
            throw new WuRuntimeException(url + "not register");
        }

        listenerMap.compute(url, (k, v) -> {
            if (v == null) {
                Set<com.github.wu.registry.api.EventListener> newSet = new HashSet<>();
                newSet.add(eventListener);
                return newSet;
            } else {
                v.add(eventListener);
                return v;
            }
        });
    }

    @Override
    public void unsubscribe(URL url, com.github.wu.registry.api.EventListener eventListener) {
        if (url == null) {
            throw new IllegalArgumentException("serviceName is null");
        }
        listenerMap.computeIfPresent(url, (k, v) -> {
            v.remove(eventListener);
            return v;
        });
    }

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
