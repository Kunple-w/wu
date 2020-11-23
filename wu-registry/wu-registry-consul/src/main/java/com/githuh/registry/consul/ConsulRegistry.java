package com.githuh.registry.consul;

import com.github.wu.common.URL;
import com.github.wu.common.URLConstant;
import com.github.wu.registry.api.UrlListener;
import com.github.wu.registry.api.RegisterService;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.cache.ConsulCache;
import com.orbitz.consul.cache.ServiceHealthCache;
import com.orbitz.consul.cache.ServiceHealthKey;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wangyongxu
 */
public class ConsulRegistry implements RegisterService {

    static final long DEFAULT_CHECK_PASS_INTERVAL = 16L;
    public static final String URL_META_KEY = "url";

    private Consul consul;

    private Map<URL, ServiceHealthCache> cacheMap = new ConcurrentHashMap<>();
    private Map<ServiceHealthCache, List<Map<UrlListener, ConsulCache.Listener<ServiceHealthKey, ServiceHealth>>>> listenerMap = new ConcurrentHashMap<>();

    public ConsulRegistry(String url) {
        if (StringUtils.isNotBlank(url)) {
            this.consul = Consul.builder().withUrl(url).build();
        } else {
            this.consul = Consul.builder().build();
        }
    }

    public ConsulRegistry() {
        this.consul = Consul.builder().build();
    }

    @Override
    public void register(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null!");
        }
        AgentClient agentClient = consul.agentClient();
        String serviceId = bulidId(url);
        if (agentClient.isRegistered(serviceId)) {
            return;
        }
        Registration registration = buildRegistration(url, serviceId);
        agentClient.register(registration);
    }

    private ImmutableRegistration buildRegistration(URL url, String serviceId) {
        String serviceName = getServiceName(url);
        Integer port = url.getPort();
        String address = url.getHost();
        return ImmutableRegistration.builder().name(serviceName).port(port).address(address).id(serviceId).check(buildRegCheck(url)).tags(buildTags(url)).meta(buildMeta(url)).build();
    }

    private Map<String, String> buildMeta(URL url) {
        return Collections.singletonMap(URL_META_KEY, url.getFullURL());
    }

    private String getServiceName(URL url) {
        return url.getParam(URLConstant.APPLICATION_KEY);
    }

    private String bulidId(URL url) {
        return Integer.toHexString(url.hashCode());
    }

    private List<String> buildTags(URL url) {
        Map<String, String> params = url.getParameters();
        List<String> tags = params.entrySet().stream().map(k -> k.getKey() + "=" + k.getValue()).collect(Collectors.toList());
        return tags;
    }

    private Registration.RegCheck buildRegCheck(URL url) {
        String checkPassInterval = url.getParam(URLConstant.CHECK_PASS_INTERVAL);
        long ttl = DEFAULT_CHECK_PASS_INTERVAL;
        if (!StringUtils.isEmpty(checkPassInterval)) {
            ttl = Long.parseLong(checkPassInterval);
        }
        Registration.RegCheck regCheck = Registration.RegCheck.ttl(ttl);
        return regCheck;
    }

    @Override
    public void unregister(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null!");
        }
        AgentClient agentClient = consul.agentClient();
        String serviceId = bulidId(url);
        agentClient.deregister(serviceId);
    }

    @Override
    public List<URL> lookup(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null!");
        }
        String serviceName = getServiceName(url);
        List<ServiceHealth> serviceHealthy = findServiceHealthy(serviceName);
        if (serviceHealthy == null || serviceHealthy.size() == 0) {
            return new ArrayList<>();
        } else {
            return convert(serviceHealthy);
        }
    }

    private List<URL> convert(List<ServiceHealth> serviceHealthy) {
        return serviceHealthy.stream().map(ServiceHealth::getService).filter(Objects::nonNull).map(Service::getMeta).filter(m -> m != null && m.containsKey(URL_META_KEY)).map(m -> m.get(URL_META_KEY)).map(URL::of).collect(Collectors.toList());
    }

    private List<ServiceHealth> findServiceHealthy(String serviceName) {
        HealthClient healthClient = consul.healthClient();
        return healthClient.getHealthyServiceInstances(serviceName).getResponse();
    }

    @Override
    public void subscribe(URL url, UrlListener urlListener) {
        HealthClient healthClient = consul.healthClient();
        String serviceName = getServiceName(url);
        ServiceHealthCache serviceHealthCache = cacheMap.get(url);
        if (serviceHealthCache == null) {
            serviceHealthCache = ServiceHealthCache.newCache(healthClient, serviceName);
        }

        ServiceHealthCache finalServiceHealthCache = serviceHealthCache;
        ConsulCache.Listener<ServiceHealthKey, ServiceHealth> listener = newValues -> {
            List<ServiceHealth> serviceHealths = newValues.entrySet().stream().filter(entry -> entry.getKey().getServiceId().equals(bulidId(url))).map(Map.Entry::getValue).collect(Collectors.toList());
            if (serviceHealths.size() > 0) {

                List<URL> urls = convert(serviceHealths);
                UrlListener.URLChanged urlChanged = new UrlListener.URLChanged(new HashSet<>(urls));
                urlListener.onEvent(urlChanged);
            }
        };
        List<Map<UrlListener, ConsulCache.Listener<ServiceHealthKey, ServiceHealth>>> maps = listenerMap.get(finalServiceHealthCache);
        Map<UrlListener, ConsulCache.Listener<ServiceHealthKey, ServiceHealth>> map = new HashMap<>();
        map.put(urlListener, listener);
        if (maps == null) {
            List<Map<UrlListener, ConsulCache.Listener<ServiceHealthKey, ServiceHealth>>> listenerList = new ArrayList<>();
            listenerList.add(map);
            listenerMap.put(finalServiceHealthCache, listenerList);
        } else {
            maps.add(map);
        }
        serviceHealthCache.addListener(listener);
        serviceHealthCache.start();
    }

    @Override
    public void unsubscribe(URL url, UrlListener urlListener) {
        ServiceHealthCache serviceHealthCache = cacheMap.get(url);
        if (serviceHealthCache != null) {
            List<Map<UrlListener, ConsulCache.Listener<ServiceHealthKey, ServiceHealth>>> maps = listenerMap.get(serviceHealthCache);
            List<Map<UrlListener, ConsulCache.Listener<ServiceHealthKey, ServiceHealth>>> list = maps.stream().filter(m -> m.get(urlListener) != null).collect(Collectors.toList());
            serviceHealthCache.removeListener(list.get(0).get(urlListener));
        }

    }

    @Override
    public URL getURL() {
        // TODO: 2020-11-13 04:26:34 未实现

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
