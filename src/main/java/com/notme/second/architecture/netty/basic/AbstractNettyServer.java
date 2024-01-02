package com.notme.second.architecture.netty.basic;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.basic.AbstractEndpoint;
import com.notme.second.architecture.apis.transport.Server;

/**
 * @author monstaxl
 **/
public abstract class AbstractNettyServer extends AbstractEndpoint implements Server {

    public AbstractNettyServer(NetworkAddress address) {
        super(address);
    }
}
