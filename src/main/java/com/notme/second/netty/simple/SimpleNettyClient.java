package com.notme.second.netty.simple;

import com.notme.second.netty.NettyMessageDecoder;
import com.notme.second.netty.NettyMessageEncoder;
import com.notme.second.trans.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @author listen
 **/
public class SimpleNettyClient {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            new Thread(() -> SimpleNettyClient.connect(7777)).start();
        }
    }

    public static void connect(int port) {
        connect("localhost", port);
    }

    public static void connect(String host, int port) {
        ClientImpl client = new ClientImpl();
        client.connect(host, port);
    }

    static class ClientImpl {
        private final Bootstrap clientBootstrap = new Bootstrap();

        private final EventLoopGroup worker = new NioEventLoopGroup();

        public void connect(String host, int port) {
            try {
                clientBootstrap.channel(NioSocketChannel.class)
                        .group(worker)
                        // 长连接
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        // 关闭NAGLE算法
                        .option(ChannelOption.TCP_NODELAY, true)
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
                Channel channelToServer = future.channel();
                String msg = "TestNettyClient try send msg to server";
                sendMsgToServer(msg,
                        10,
                        channelToServer);
                // 等待关闭
                channelToServer.closeFuture().sync();
            } catch (Exception e) {
                System.err.println("Error creating client binding {" + host + ":" + port + "}");
                e.printStackTrace();
            } finally {
                worker.shutdownGracefully();
            }
        }

        private void sendMsgToServer(String template, int times, final Channel channel) {
            functionalApi(template, 0, times, channel);
        }

        private void functionalApi(String template, int cur, int end, final Channel channel) {
            if (cur < 10) {
                final String finalStr = Thread.currentThread().getName() + " " + template + cur;
                System.out.println("发送: " + finalStr);
                byte[] body = finalStr.getBytes(StandardCharsets.UTF_8);
                int length = body.length;
                channel.writeAndFlush(
                        new Message.Builder().bizCode(1).body(body).bodyLength(length).build() // ok
//                        finalStr // error
                ).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        // 这里是不需要 while循环去判断 isDone的
                        // 因为这里是 operationComplete的回调，完成了才会到这里，加多一次判断是为了确保
                        if (future.isDone()) {
                            if (future.cause() != null) {
                                // todo 必须要在这里进行检测才知道是不是真的发送出去了
                                System.out.println(Thread.currentThread().getName() + " wrote error: " + future.cause().getMessage());
                            }
                        }
                    }
                });
                functionalApi(template, cur + 1, end, channel);
            }
        }
    }

}
