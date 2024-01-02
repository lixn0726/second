package com.notme.second.architecture.netty;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Server;
import com.notme.second.architecture.apis.transport.basic.AbstractEndpoint;
import com.notme.second.architecture.netty.codec.NettyMessageDecoder;
import com.notme.second.architecture.netty.codec.NettyMessageEncoder;
import com.notme.second.architecture.netty.common.handler.EchoMsgHandler;
import com.notme.second.architecture.netty.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author monstaxl
 * todo: 继承关系改一下，把Server和AbstractEndpoint合并到一个抽象类里。由于现在我还没想好Server具体要有什么，所以先这么写
 **/
public class NettyServer extends AbstractEndpoint implements Server {

    private static final boolean useEpoll = NettyEnvironment.useEpoll();

    private final NettyServerHandler nettyServerHandler = new NettyServerHandler();

    private final ServerBootstrap serverBootstrap = new ServerBootstrap();

    private final EventLoopGroup boss = bossEventLoopGroup();

    private final EventLoopGroup worker = workerEventLoopGroup();

    public NettyServer(String ip, Integer port) {
        this(new NetworkAddress(ip, port));
    }

    public NettyServer(NetworkAddress bindAddress) {
        super(bindAddress);
    }

    @Override
    public NetworkAddress address() {
        return this.address;
    }

    @Override
    public void start() {
        Integer port = address.getPort();
        try {
            serverBootstrap.channel(serverChannelClass())
                    .group(boss, worker)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<ServerSocketChannel>() {

                        @Override
                        protected void initChannel(ServerSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(nettyServerHandler);
                        }
                    })
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyMessageEncoder());
                            pipeline.addLast(new NettyMessageDecoder());
                            pipeline.addLast(new NettyServerHandler());
                            pipeline.addLast(new EchoMsgHandler());
                        }
                    });

            ChannelFuture bindFuture = serverBootstrap.bind(port)
                    .sync();
            bindFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.err.println("Error creating server binding {" + port + "}");
            e.printStackTrace();
        } finally {
            // todo: shutdownHook or finally ?
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public Collection<Channel> channels() {
        return null;
    }

    @Override
    public Channel fetchChannel(Predicate<?> condition) {
        return null;
    }

    private static Class<? extends ServerChannel> serverChannelClass() {
        return useEpoll ?
                EpollServerSocketChannel.class :
                NioServerSocketChannel.class;
    }

    private static EventLoopGroup bossEventLoopGroup() {
        return decideEventLoopGroup(1);
    }

    private static EventLoopGroup workerEventLoopGroup() {
        return decideEventLoopGroup(NettyEnvironment.workerEventLoopThreadNumber());
    }

    private static EventLoopGroup decideEventLoopGroup(final int nThread) {
        return useEpoll ?
                new EpollEventLoopGroup(nThread) :
                new NioEventLoopGroup(nThread);
    }


}
