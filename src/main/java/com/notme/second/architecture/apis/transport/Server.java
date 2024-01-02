package com.notme.second.architecture.apis.transport;

import com.notme.second.architecture.NetworkAddress;
import io.netty.channel.Channel;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author monstaxl
 **/
public interface Server extends Endpoint {

    Collection<Channel> channels();

    // todo: Predicate的泛型需要给个具体的定义，不然API都不好写
    Channel fetchChannel(Predicate<?> condition);
}
