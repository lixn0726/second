package com.notme.second.architecture.netty.handler;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Channel;
import com.notme.second.architecture.netty.NettyChannel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author monstaxl
 * 装配到每一个 Netty ChannelPipeline上，统一管理 Channel
 **/
@ChannelHandler.Sharable
public class NettyChannelManager extends ChannelDuplexHandler {

    private static final Logger log = LoggerFactory.getLogger(NettyChannelManager.class);

    private static final Function<ChannelHandlerContext, NetworkAddress> remoteAddressParser = ctx ->
            NetworkAddress.fromSocketAddress(ctx.channel().remoteAddress());

    private final Map<NetworkAddress, NettyChannel> nettyChannelMap = new ConcurrentHashMap<>();

    public NettyChannelManager() {
    }

    public List<NettyChannel> nettyChannels() {
        return new ArrayList<>(nettyChannelMap.values());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NetworkAddress remote = remoteAddressParser.apply(ctx);
        if (nettyChannelMap.containsKey(remote)) {
            log.error("[NettyServerHandler] Existing channel from {}, closed existed and use the latest channel.", remote);
            NettyChannel old = nettyChannelMap.get(remote);
            // todo: 这里，可以引入自己的事件机制实现一些回调
            old.shutdown();
        }
        log.info("[NettyServerHandler] Detecting establishing channel from {}.", remote);
        nettyChannelMap.put(remote, NettyChannel.getOrCreate(ctx.channel()));
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        NetworkAddress remote = remoteAddressParser.apply(ctx);
        log.info("[NettyServerHandler] Detecting closing channel from {}.", remote);
        nettyChannelMap.remove(remote);
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            NetworkAddress remote = remoteAddressParser.apply(ctx);
            log.warn("[NettyServerHandler] Detecting channel from {} timeout and trigger idle event, close it.", remote);
            closeAndRemoveChannel(remote);
        }
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        NetworkAddress remote = remoteAddressParser.apply(ctx);
        if (nettyChannelMap.containsKey(remote)) {
            log.error("[NettyServerHandler] Detecting handling channel from {} occur error, close it.", remote, cause);
            closeAndRemoveChannel(remote);
        }
        ctx.fireExceptionCaught(cause);
    }

    private void closeAndRemoveChannel(NetworkAddress remote) {
        try {
            nettyChannelMap.get(remote).shutdown();
        } finally {
            nettyChannelMap.remove(remote);
        }
    }

}
