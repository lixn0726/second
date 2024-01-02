package com.notme.second.architecture.apis.transport;

import com.notme.second.architecture.NetworkAddress;

/**
 * @author monstaxl
 **/
public interface Channel {

    NetworkAddress remoteAddress();

    boolean isConnecting();

    void send(Object msg);

    void send(Object msg, boolean async);

    // todo
    //  send：放入队列等待发送
    //  sendAndFlush：直接发送
    default void sendAndFlush(Object msg) {
    }

    default void sendAndFlush(Object msg, boolean async) {
    }

}
