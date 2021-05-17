package com.github.wu.core.rpc.remoting.transport;

import java.net.InetSocketAddress;

/**
 * @author wangyongxu
 */
public interface EndPointFactory {

    Server createServer(int port);

    Client createClient(InetSocketAddress remote);
}
