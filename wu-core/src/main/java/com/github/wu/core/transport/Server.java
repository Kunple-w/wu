package com.github.wu.core.transport;

import com.github.wu.common.exception.WuRuntimeException;
import com.github.wu.core.register.Provider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 服务端
 *
 * @author wangyongxu
 */
public class Server {

    private Thread thread;


    private final ServerHandler serverHandler = new ServerHandler();

    private final InetSocketAddress inetSocketAddress;

    public Server(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }

    /**
     * start a server
     */
    public void start() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                bind(inetSocketAddress);
            }
        }, "serverStartThread");
        thread.start();
    }

    public void registerProvider(Provider<?> provider) {
        serverHandler.registerProvider(provider);
    }

    public void unregisterProvider(Provider<?> provider) {
        serverHandler.unregisterProvider(provider);
    }

    /**
     * close a server
     */
    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    private void bind(InetSocketAddress inetSocketAddress) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    socketChannel.pipeline().addLast(new IdleStateHandler(0, 0, 30 * 3, TimeUnit.SECONDS));
                    socketChannel.pipeline().addLast(new SengMessageDecoder());
                    socketChannel.pipeline().addLast(new SengMessageEncoder());
                    socketChannel.pipeline().addLast(serverHandler);
                }
            }).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind(inetSocketAddress.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new WuRuntimeException("start server fail.", e);
        } catch (Throwable t) {
            throw new WuRuntimeException("unknown error", t);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
