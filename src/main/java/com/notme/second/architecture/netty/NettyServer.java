package com.notme.second.architecture.netty;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Channel;
import com.notme.second.architecture.apis.transport.Server;
import com.notme.second.architecture.apis.transport.basic.AbstractEndpoint;
import com.notme.second.architecture.netty.codec.NettyMessageDecoder;
import com.notme.second.architecture.netty.codec.NettyMessageEncoder;
import com.notme.second.architecture.netty.common.NettyConstants;
import com.notme.second.architecture.netty.common.NettyResources;
import com.notme.second.architecture.netty.handler.NettyChannelManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @author monstaxl
 * todo: 继承关系改一下，把Server和AbstractEndpoint合并到一个抽象类里。由于现在我还没想好Server具体要有什么，所以先这么写
 **/
public class NettyServer extends AbstractEndpoint implements Server {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    // static fields and functions

    private static final boolean useEpoll = NettyEnvironment.useEpoll();

    // object fields

    private final NettyChannelManager nettyChannelManager = new NettyChannelManager();

    private final ServerBootstrap serverBootstrap = new ServerBootstrap();

    private final EventLoopGroup boss = bossEventLoopGroup();

    private final EventLoopGroup worker = workerEventLoopGroup();

    private volatile io.netty.channel.Channel channel;

    public NettyServer() {
        this(NettyResources.randomPort());
    }

    public NettyServer(Integer port) {
        this(NettyConstants.LOCALHOST_STR, port);
    }

    public NettyServer(String ip, Integer port) {
        this(new NetworkAddress(ip, port));
    }

    public NettyServer(NetworkAddress bindAddress) {
        super(bindAddress);
        initServerBootstrap();
    }

    @Override
    public NetworkAddress address() {
        return this.address;
    }

    @Override
    public void start() {
        String host = address.getIp();
        Integer port = address.getPort();
        try {
            ChannelFuture bindFuture = serverBootstrap.bind(host, port).sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isDone()) {
                        if (future.isSuccess()) {
                            log.info("[NettyServer] Bootstrapping done. Binding {}", address);
                        } else if (Objects.nonNull(future.cause())) {
                            log.error("[NettyServer] Bootstrapping error. Binding {}", address, future.cause());
                        }
                    }
                }
            });
            this.channel = bindFuture.channel();
            // wait for closing.
            this.channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("[NettyServer] Error creating server binding {}", address, e);
        }
    }

    @Override
    public void shutdown() {
        try {
            for (Channel established : channels()) {
                established.shutdown();
            }
            channel.close();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    // modify the return type. cast to NettyChannel
    @Override
    public Collection<NettyChannel> channels() {
        return nettyChannelManager.nettyChannels();
    }

    @Override
    public Channel fetchChannel(Predicate<? super Channel> condition) {
        for (Channel channel : channels()) {
            if (condition.test(channel)) {
                return channel;
            }
        }
        return null;
    }

    private void initServerBootstrap() {
        serverBootstrap.channel(serverChannelClass())
                .group(boss, worker)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_KEEPALIVE, NettyEnvironment.channelKeepAlive())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // todo: 业务上的 ServerHandler
                        pipeline.addLast(new NettyMessageEncoder(),
                                new NettyMessageDecoder(),
                                new IdleStateHandler(0, 0, 1, TimeUnit.MINUTES),
                                nettyChannelManager);
                    }
                });
    }

    private static Class<? extends ServerChannel> serverChannelClass() {
        return useEpoll ?
                EpollServerSocketChannel.class :
                NioServerSocketChannel.class;
    }

    private static EventLoopGroup bossEventLoopGroup() {
        return NettyResources.bossEventLoopGroup(NettyEnvironment.bossEventLoopGroupThreads(),
                "NettyServer-BossThread");
    }

    private static EventLoopGroup workerEventLoopGroup() {
        return NettyResources.workerEventLoopGroup(NettyEnvironment.workerEventLoopGroupThreads(),
                "NettyServer-WorkerThread");
    }

}
