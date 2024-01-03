package com.notme.second.architecture.netty;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author monstaxl
 **/
public class NettyChannel implements Channel {

    private static final Logger log = LoggerFactory.getLogger(NettyChannel.class);

    private static final Map<io.netty.channel.Channel, NettyChannel> NETTY_CHANNEL_MAP = new ConcurrentHashMap<>();

    private final io.netty.channel.Channel channel;

    private volatile boolean active = false;

    private NettyChannel(io.netty.channel.Channel channel) {
        this.channel = channel;
    }

    public static NettyChannel getOrCreate(io.netty.channel.Channel channel) {
        if (Objects.isNull(channel)) {
            log.error("[NettyChannel] Trying to get or create an instance for a null.");
            return null;
        }
        if (NETTY_CHANNEL_MAP.containsKey(channel)) {
            return NETTY_CHANNEL_MAP.get(channel);
        }
        NettyChannel created = new NettyChannel(channel);
        NETTY_CHANNEL_MAP.put(channel, created);
        return created;
    }

    @Override
    public NetworkAddress remoteAddress() {
        if (isConnected()) {
            return NetworkAddress.fromSocketAddress(channel.remoteAddress());
        }
        throw new IllegalStateException("Netty Channel {" + channel.id() + "} is not connecting. " +
                "Cannot detect the remote address.");
    }

    @Override
    public boolean isConnecting() {
        return channel.isOpen() && !channel.isActive();
    }

    @Override
    public boolean isConnected() {
        return channel.isOpen() && channel.isActive();
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean isDisconnecting() {
        return channel.isOpen() && !channel.isActive();
    }

    @Override
    public boolean isDisconnected() {
        return !channel.isOpen();
    }

    @Override
    public void send(Object msg) {
        if (isConnected()) {
            send(msg, false);
        } else {
            throw new IllegalStateException("Netty Channel {" + channel.id() + "} is not connecting. " +
                    "Cannot send message.");
        }
    }

    @Override
    public void send(Object msg, boolean async) {
        if (isConnected()) {
            CountDownLatch cdl = new CountDownLatch(1);
            try {
                // todo: 发送的逻辑
            } finally {

            }
        } else {
            throw new IllegalStateException("Netty Channel {" + channel.id() + "} is not connecting. " +
                    "Cannot send message.");
        }
    }

    // todo: 应该只留下shutdown / close方法就行

    @Override
    public String id() {
        return null;
    }

    @Override
    public NetworkAddress address() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
