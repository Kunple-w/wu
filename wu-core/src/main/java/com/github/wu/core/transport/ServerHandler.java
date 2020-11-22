package com.github.wu.core.transport;

import com.github.wu.core.register.Provider;
import com.github.wu.core.rpc.exception.ServiceNotRegisterException;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty服务端处理器
 *
 * @author wangyongxu
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private static final Map<String, Provider<?>> map = new ConcurrentHashMap<>();

    public void registerProvider(Provider<?> provider) {
        map.put(provider.getInterface().getName(), provider);
    }

    public void unregisterProvider(Provider<?> provider) {
        map.remove(provider.getInterface().getName());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("收到消息: {}", msg);
        if (msg instanceof Request) {
            Request request = (Request) msg;
            ApiResult body = handleRequest(request);
            Response response = new Response(request, body);
            ctx.channel().writeAndFlush(response);
            return;
        }
        super.channelRead(ctx, msg);
    }

    private ApiResult handleRequest(Request request) {
        Invocation invocation = request.getBody();
        String serviceName = invocation.getServiceName();
        Provider<?> provider = getService(serviceName);
        if (provider == null) {
            throw new ServiceNotRegisterException(serviceName + " not existed");
        }
        logger.info("provider find: {}", provider);
        ApiResult apiResult = provider.call(request.getBody());
        logger.info("provider call result: {}", apiResult);
        return apiResult;
    }

    private Provider<?> getService(String serviceName) {
        return map.get(serviceName);
    }
}
