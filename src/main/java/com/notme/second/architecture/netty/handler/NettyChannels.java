package com.notme.second.architecture.netty.handler;

import com.notme.second.architecture.apis.transport.Channel;
import com.notme.second.architecture.apis.transport.Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author monstaxl
 **/
public class NettyChannels {

    private final Server nettyServer;

    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    public NettyChannels(Server nettyServer) {
        this.nettyServer = nettyServer;
    }

    public void addChannel(String addrStr, Channel channel) {
        this.channelMap.putIfAbsent(addrStr, channel);
    }



}
