package com.notme.second.architecture.netty;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Client;
import com.notme.second.architecture.netty.codec.NettyMessageDecoder;
import com.notme.second.architecture.netty.codec.NettyMessageEncoder;
import com.notme.second.architecture.netty.common.handler.ShowMsgHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author monstaxl
 **/
public class NettyClient implements Client {

    private NetworkAddress address;

    private Bootstrap clientBootstrap = new Bootstrap();

    private EventLoopGroup worker = new NioEventLoopGroup();

    private volatile Channel channel;

    @Override
    public void connect(NetworkAddress address) {
        String host = this.address.getIp();
        Integer port = this.address.getPort();
        try {
            clientBootstrap.channel(NioSocketChannel.class)
                    .group(worker)
                    // 长连接
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 关闭NAGLE算法
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 指定默认的ByteBuf Allocator
                    // todo 不同的Allocator的使用上的区别，我自己记得的是 ComposeXXX, DirectXXX, DefaultXXX
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    // 连接超时时长
//                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyMessageEncoder());
                            pipeline.addLast(new NettyMessageDecoder());
                            pipeline.addLast(new ShowMsgHandler());
                        }
                    });

            ChannelFuture future = clientBootstrap.connect(host, port).sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isDone()) {
                        if (future.cause() == null
                                && future.isSuccess()) {
                            System.out.println(Thread.currentThread().getName() + " connect to server {" +
                                    host + ":" + port +
                                    "} success.");
                        }
                    }
                }
            });
            this.channel = future.channel();
            // 等待关闭
            hangingCurrentThreadUntilBootstrapIsClosed();
        } catch (Exception e) {
            System.err.println("Error creating client binding {" + host + ":" + port + "}");
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }

    private void hangingCurrentThreadUntilBootstrapIsClosed() {
        try {
            // reordered？
            this.channel.closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("Hanging ClientThread error: {[" + e.getMessage() + "]}.");
        }
    }

    @Override
    public NetworkAddress address() {
        return this.address;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

}
