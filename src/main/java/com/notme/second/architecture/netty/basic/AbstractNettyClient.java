package com.notme.second.architecture.netty.basic;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.basic.AbstractEndpoint;
import com.notme.second.architecture.apis.transport.Client;

/**
 * @author monstaxl
 **/
public abstract class AbstractNettyClient extends AbstractEndpoint implements Client {

    public AbstractNettyClient(NetworkAddress address) {
        super(address);
    }

}
