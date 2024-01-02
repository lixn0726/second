package com.notme.second.architecture.netty;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CountDownLatch;

/**
 * @author monstaxl
 **/
public class NettyChannel implements Channel {

    private final io.netty.channel.Channel channel;

    private NetworkAddress remoteAddress;

    private NettyChannel(io.netty.channel.Channel channel) {
        this.channel = channel;
    }

    public static NettyChannel getOrCreate(String addr, io.netty.channel.Channel channel) {
        // todo
        NettyChannel res = new NettyChannel(channel);
        return res;
    }

    @Override
    public NetworkAddress remoteAddress() {
        if (isConnecting()) {
            return NetworkAddress.fromSocketAddress(channel.remoteAddress());
        }
        throw new IllegalStateException("Netty Channel {" + channel.id() + "} is not connecting. " +
                "Cannot detect the remote address.");
    }

    @Override
    public boolean isConnecting() {
        return false;
    }

    @Override
    public void send(Object msg) {
        send(msg, false);
    }

    @Override
    public void send(Object msg, boolean async) {
        CountDownLatch cdl = new CountDownLatch(1);
        try {
            channel.writeAndFlush(msg)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isDone()) {
                                if (future.isSuccess()) {
                                    cdl.countDown();
                                }
                            }
                        }
                    });

        } finally {

        }
    }
}
