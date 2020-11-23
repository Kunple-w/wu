package com.github.wu.registry.zookeeper;

import com.github.wu.common.URL;
import com.github.wu.common.URLConstant;
import com.github.wu.common.exception.WuRuntimeException;
import com.github.wu.common.spi.SPIAlias;
import com.github.wu.registry.api.UrlListener;
import com.github.wu.registry.api.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wangyongxu
 */
@SPIAlias(alias = "zookeeper")
public class ZookeeperRegistry implements RegisterService {

    private URL url;

    private CuratorFramework client;
    private String root;
    private static final String DEFAULT_ROOT = "/wu";
    private static final String PATH_SEPARATOR = "/";

    private final Map<URL, CuratorCache> listenerMap = new ConcurrentHashMap<>();

    public ZookeeperRegistry(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void init() {
        startClientIfNeed();
    }

    @Override
    public void destroy() {
    }

    private void startClientIfNeed() {
        CuratorFrameworkState curatorFrameworkState = client.getState();
        if (curatorFrameworkState == CuratorFrameworkState.LATENT) {
            client.start();
        }
    }

    @Override
    public void register(URL url) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(toZkPath(url), URLEncoder.encode(url.toString(), "UTF-8").getBytes());
        } catch (Exception exception) {
            throw new WuRuntimeException(exception);
        }
    }

    private String toZkPath(URL url) {
        return toParentPath(url) + PATH_SEPARATOR + url.getIpAndPort();
    }

    private String toParentPath(URL url) {
        if (url.getPath().endsWith(PATH_SEPARATOR)) {
            return getRoot() + url.getPath() + url.getParam(URLConstant.CATEGORY_KEY, URLConstant.PROVIDERS_CATEGORY);
        }
        return getRoot() + url.getPath() + PATH_SEPARATOR + url.getParam(URLConstant.CATEGORY_KEY, URLConstant.PROVIDERS_CATEGORY);
    }

    public String getRoot() {
        return StringUtils.isEmpty(root) ? DEFAULT_ROOT : root;
    }

    @Override
    public void unregister(URL url) {
        try {
            client.delete().forPath(toZkPath(url));
        } catch (Exception exception) {
            throw new WuRuntimeException(exception);
        }
    }

    @Override
    public List<URL> lookup(URL url) {
        String parentPath = toParentPath(url);
        try {
            List<String> children = client.getChildren().forPath(parentPath);
            List<URL> urls = new ArrayList<>(children.size());
            for (String child : children) {
                byte[] bytes = client.getData().forPath(parentPath + PATH_SEPARATOR + child);
                URL of = URL.of(URLDecoder.decode(new String(bytes), "UTF-8"));
                urls.add(of);
            }
            return urls;
        } catch (Exception exception) {
            return Collections.emptyList();
        }
    }

    private CuratorCache createListener(URL url, UrlListener URLListener) {
        String toParentPath = toParentPath(url);
        CuratorCache curatorCache = CuratorCache.build(client, toParentPath);
        CuratorCacheListener cacheListener = CuratorCacheListener.builder().forPathChildrenCache(toParentPath, client, new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                PathChildrenCacheEvent.Type type = event.getType();
                switch (type) {
                    case CHILD_ADDED:
                    case CHILD_UPDATED:
                    case CHILD_REMOVED: {
                        onEvent(client, event);
                        break;
                    }
                    case CONNECTION_SUSPENDED:
                    case CONNECTION_RECONNECTED:
                    case CONNECTION_LOST:
                    case INITIALIZED:
                        break;
                }
            }

            private void onEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData childData = event.getData();
                String dataPath = childData.getPath();
                int lastIndexOf = dataPath.lastIndexOf(PATH_SEPARATOR);
                String parentPath = dataPath.substring(0, lastIndexOf);
                List<String> childrenPath = client.getChildren().forPath(parentPath).stream().map(e -> parentPath + PATH_SEPARATOR + e).collect(Collectors.toList());
                Set<URL> urls = new HashSet<>();
                for (String childPath : childrenPath) {
                    byte[] bytes = client.getData().forPath(childPath);
                    URL childUrl = URL.of(URLDecoder.decode(new String(bytes), "UTF-8"));
                    urls.add(childUrl);
                }
                UrlListener.URLChanged urlChanged = new UrlListener.URLChanged(urls);
                URLListener.onEvent(urlChanged);
            }
        }).build();
        curatorCache.listenable().addListener(cacheListener);
        return curatorCache;
    }

    @Override
    public void subscribe(URL url, UrlListener urlListener) {
        CuratorCache curatorCache = createListener(url, urlListener);
        curatorCache.start();
        listenerMap.put(url, curatorCache);
    }

    @Override
    public void unsubscribe(URL url, UrlListener urlListener) {
        CuratorCache curatorCache = listenerMap.get(url);
        if (curatorCache != null) {
            curatorCache.close();
        }
    }
}
