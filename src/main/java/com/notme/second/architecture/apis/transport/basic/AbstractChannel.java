package com.notme.second.architecture.apis.transport.basic;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.apis.transport.Channel;

/**
 * @author monstaxl
 **/
public abstract class AbstractChannel extends AbstractEndpoint implements Channel {

    public AbstractChannel(NetworkAddress address) {
        super(address);
    }
}
