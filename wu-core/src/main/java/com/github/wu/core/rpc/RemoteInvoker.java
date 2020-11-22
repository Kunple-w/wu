package com.github.wu.core.rpc;

import com.github.wu.common.URL;
import com.github.wu.common.exception.WuRuntimeException;
import com.github.wu.core.transport.*;

import java.net.InetSocketAddress;

/**
 * @author wangyongxu
 */
public class RemoteInvoker<T> implements Invoker<T> {

    private final EndPointFactory endPointFactory = new EndPointFactoryImpl();

    private final URL url;

    private final Class<T> interfaceClass;

    private Client client;

    public RemoteInvoker(URL url, Class<T> interfaceClass) {
        this.url = url;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public ApiResult call(Invocation invocation) {
        init();
        Request request = new Request(invocation);
        Object body = client.send(request).getBody();
        if (body instanceof ApiResult) {
            return (ApiResult) body;
        }
        throw new WuRuntimeException("not support: " + body.getClass());
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return client.isActive();
    }

    @Override
    public void init() {
        if (client == null) {
            client = endPointFactory.createClient(new InetSocketAddress(url.getHost(), url.getPort()));
        }
    }

    @Override
    public void destroy() {
        client.disConnect();
    }
}
