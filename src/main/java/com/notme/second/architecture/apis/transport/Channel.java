package com.notme.second.architecture.apis.transport;

import com.notme.second.architecture.NetworkAddress;

/**
 * @author monstaxl
 **/
public interface Channel extends Endpoint {

    NetworkAddress remoteAddress();

    boolean isConnecting();

    boolean isConnected();

    boolean isDisconnecting();

    boolean isDisconnected();

    boolean isActive();

    void send(Object msg, boolean async);

    // todo
    //  send：放入队列等待发送
    //  sendAndFlush：直接发送
    default void sendAndFlush(Object msg) {
    }

    default void sendAndFlush(Object msg, boolean async) {
    }

}
