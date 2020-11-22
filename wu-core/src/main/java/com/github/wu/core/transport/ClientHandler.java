package com.github.wu.core.transport;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * Client handler
 *
 * @author qiankewei
 */
public class ClientHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private Map<String, SynchronousQueue<Response>> queueMap = new ConcurrentHashMap<>();

    private static Map<Class<?>, Object> proxyMap = new HashMap<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel注册ok");
        queueMap.put(ctx.channel().id().toString(), new SynchronousQueue<>());
        super.channelRegistered(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logger.info("write: {}", msg);
        super.write(ctx, msg, promise);
    }


    public Response getResponse(Channel channel) {
        try {
            return queueMap.get(channel.id().toString()).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SynchronousQueue<Response> synchronousQueue = queueMap.get(ctx.channel().id().toString());
        synchronousQueue.offer((Response) msg);
        logger.info("客户当收到消息: {}", msg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("客户端异常", cause);
        super.exceptionCaught(ctx, cause);
    }

}
