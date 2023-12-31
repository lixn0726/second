package com.notme.second.architecture.communication.defaults.netty;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.architecture.communication.EmbeddedServer;

/**
 * @author monstaxl
 **/
public class EmbeddedNettyServer implements EmbeddedServer {

    @Override
    public void bind(NetworkAddress address) {

    }

    @Override
    public NetworkAddress address() {
        return null;
    }

    @Override
    public void setAddress(NetworkAddress address) {

    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
