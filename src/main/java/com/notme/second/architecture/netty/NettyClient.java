package com.notme.second.architecture.netty;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Client;
import com.notme.second.architecture.apis.transport.basic.AbstractEndpoint;
import com.notme.second.architecture.netty.codec.NettyMessageDecoder;
import com.notme.second.architecture.netty.codec.NettyMessageEncoder;
import com.notme.second.architecture.netty.common.NettyConstants;
import com.notme.second.architecture.netty.common.NettyResources;
import com.notme.second.architecture.netty.common.handler.ShowMsgHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author monstaxl
 **/
public class NettyClient extends AbstractEndpoint implements Client {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private final Bootstrap clientBootstrap = new Bootstrap();

    private final EventLoopGroup worker = workerEventLoopGroup();

    private volatile Channel channel;

    public NettyClient(Integer port) {
        this(NettyConstants.LOCALHOST_STR, port);
    }

    public NettyClient(String host, Integer port) {
        this(new NetworkAddress(host, port));
    }

    public NettyClient(NetworkAddress address) {
        super(address);
        initBootstrap();
    }

    @Override
    public void connect(NetworkAddress address) {
        if (Objects.isNull(address)) {
            log.warn("[NettyClient] Input argument for method#connect is null, use the constructor argument.");
        }
        if (!address.equals(this.address)) {
            log.warn("[NettyClient] Input argument for method#connect is not the " +
                    "same as the constructor argument, use current argument {}", address);
            setAddress(address);
        }
        start();
    }

    @Override
    public void start() {
        String host = this.address.getIp();
        Integer port = this.address.getPort();
        try {
            ChannelFuture connectFuture = clientBootstrap.connect(host, port).sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isDone()) {
                        if (future.isSuccess()) {
                            log.info("[NettyClient] Bootstrapping done. Connecting {}", address);
                        } else if (Objects.nonNull(future.cause())) {
                            log.error("[NettyClient] Bootstrapping error. Connecting {}", address, future.cause());
                        }
                    }
                }
            });
            this.channel = connectFuture.channel();
            // 等待关闭
            this.channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("[NettyClient] Error creating client connecting {}", address, e);
        }
    }

    @Override
    public void shutdown() {
        try {
            this.channel.close();
        } finally {
            worker.shutdownGracefully();
        }
    }

    @Override
    public void send(Object msg) {
        this.channel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isDone()) {
                    if (future.isSuccess()) {
                        log.info("[NettyClient] Sending {} done.", msg);
                    } else if (Objects.nonNull(future.cause())) {
                        log.error("[NettyClient] Sending {} error.", msg, future.cause());
                    }
                }
            }
        });
    }

    private EventLoopGroup workerEventLoopGroup() {
        return NettyResources.workerEventLoopGroup(NettyEnvironment.workerEventLoopGroupThreads(),
                "NettyClient-WorkerThread");
    }

    private void initBootstrap() {
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
                        // todo: 业务上的 ClientHandler
                        pipeline.addLast(new NettyMessageEncoder(),
                                new NettyMessageDecoder(),
                                new ShowMsgHandler()
                        );
                    }
                });
    }

}
