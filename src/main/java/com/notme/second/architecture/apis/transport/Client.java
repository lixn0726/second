package com.notme.second.architecture.apis.transport;

import com.notme.second.architecture.NetworkAddress;

/**
 * @author monstaxl
 **/
public interface Client extends Endpoint {

    void connect(NetworkAddress address);

}
