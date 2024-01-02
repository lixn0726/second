package com.notme.second.architecture.apis.transport;

/**
 * @author monstaxl
 **/
public interface ChannelHandler {

    void onConnected(Channel channel);

    void onDisconnected(Channel channel);

    void onMessageReceived(Channel channel, Object msg);

    void onMessageSend(Channel channel, Object msg);

    void onExceptionOccur(Channel channel, Throwable throwable);

}
