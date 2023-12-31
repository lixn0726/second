package com.notme.second.netty.simple;

import com.notme.second.architecture.communication.defaults.netty.codec.NettyMessageDecoder;
import com.notme.second.architecture.communication.defaults.netty.codec.NettyMessageEncoder;
import com.notme.second.architecture.communication.defaults.netty.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author monstaxl
 **/
public class SimpleNettyServer {

    public static void main(String[] args) {
        SimpleNettyServer.start(7777);
    }

    public static void start(int port) {
        new ServerImpl().start(port);
    }

    static class ServerImpl {
        private final ServerBootstrap serverBootstrap = new ServerBootstrap();

        private final EventLoopGroup boss = new NioEventLoopGroup(1);

        private final EventLoopGroup worker = new NioEventLoopGroup();

        public void start(int port) {
            try {
                serverBootstrap.channel(NioServerSocketChannel.class)
                        .group(boss, worker)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<ServerSocketChannel>() {

                            @Override
                            protected void initChannel(ServerSocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new ConnectionEstablishListener());
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
                boss.shutdownGracefully();
                worker.shutdownGracefully();
            }


        }
    }

}
