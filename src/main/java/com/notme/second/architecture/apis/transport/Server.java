package com.notme.second.architecture.apis.transport;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author monstaxl
 **/
public interface Server extends Endpoint {

    Collection<? extends Channel> channels();

    Channel fetchChannel(Predicate<? super Channel> condition);

    // todo: 添加具体实现
    default void send(Object msg) {}

    default void send(Object msg, Channel channel) {};

    default void send(Object msg, Collection<Channel> channels) {};
}
