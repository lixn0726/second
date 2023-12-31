package com.notme.second.netty.controlling;

import com.notme.second.common.logger.Log;
import com.notme.second.common.logger.LoggerFactory;
import com.notme.second.netty.NettyEnvironment;
import com.notme.second.architecture.communication.defaults.netty.codec.NettyMessageDecoder;
import com.notme.second.architecture.communication.defaults.netty.codec.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author monstaxl
 **/
public class NettyControllerServer {

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup connReceiver;

    private EventLoopGroup connHandler;

    private static final Log log = LoggerFactory.of(NettyControllerServer.class);

    private static final AtomicInteger currentInstanceWorkerThreadCounter = new AtomicInteger(0);

    private void init() throws Exception {
        this.serverBootstrap = new ServerBootstrap();
        int handlerThreadCount = NettyEnvironment.suitableHandlerThreadCount();
        boolean epoll = NettyEnvironment.useEpoll();
        if (epoll) {
            connReceiver = new EpollEventLoopGroup(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("second_boss_thread");
                    return t;
                }
            });
            connReceiver = new EpollEventLoopGroup(handlerThreadCount, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    String tName = "second_worker_thread_" + currentInstanceWorkerThreadCounter.getAndIncrement();
                    t.setName(tName);
                    log.info("Starting Thread {" + tName + "} at {" + System.currentTimeMillis() + "}");
                    return t;
                }
            });
        }

        serverBootstrap.channel(epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new NettyMessageDecoder());
                        pipeline.addLast(new NettyMessageEncoder());
                    }
                });
    }

    private void bind() {

    }

    public void start() throws Exception {
        init();
    }


}