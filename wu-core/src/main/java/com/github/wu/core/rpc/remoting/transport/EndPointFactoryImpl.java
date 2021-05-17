package com.github.wu.core.rpc.remoting.transport;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyongxu
 */
public class EndPointFactoryImpl implements EndPointFactory {

    private static final Map<Integer, Server> serverCache = new HashMap<>();

    @Override
    public synchronized Server createServer(int port) {
        return serverCache.computeIfAbsent(port, k -> {
            Server server = new Server(new InetSocketAddress(port));
            server.start();
            return server;
        });
    }

    @Override
    public Client createClient(InetSocketAddress remote) {
        Client client = new Client(remote);
        client.start();
        return client;
    }
}
