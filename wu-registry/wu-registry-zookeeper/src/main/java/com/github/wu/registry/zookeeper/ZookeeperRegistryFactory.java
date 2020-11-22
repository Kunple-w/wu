package com.github.wu.registry.zookeeper;

import com.github.wu.common.spi.SPIAlias;
import com.github.wu.registry.api.AbstractRegistryFactory;
import com.github.wu.registry.api.RegisterService;
import com.github.wu.registry.api.exception.RegistryCreatedFailed;
import com.github.wu.common.URL;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author wangyongxu
 */
@SPIAlias(alias = "zookeeper")
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    @Override
    public RegisterService createRegistry(URL url) throws RegistryCreatedFailed {
        try {
            CuratorFramework curatorFramework = createCuratorFramework(url);
            curatorFramework.start();
            return new ZookeeperRegistry(curatorFramework);
        } catch (Exception e) {
            throw new RegistryCreatedFailed("create registry failed: " + url, e);
        }
    }

    private CuratorFramework createCuratorFramework(URL url) {
        String uri = url.getUri();
        String connectString = uri.substring(uri.indexOf("://") + 3);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        return CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    }
}
