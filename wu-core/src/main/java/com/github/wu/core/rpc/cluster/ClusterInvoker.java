package com.github.wu.core.rpc.cluster;

import com.github.wu.common.URL;
import com.github.wu.core.rpc.Invoker;
import com.github.wu.registry.api.RegisterService;

/**
 * @author wangyongxu
 */
public interface ClusterInvoker<T> extends Invoker<T> {


    URL getRegistryURL();

    RegisterService getRegistry();


}

