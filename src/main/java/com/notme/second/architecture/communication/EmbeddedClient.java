package com.notme.second.architecture.communication;

import com.notme.second.architecture.NetworkAddress;

/**
 * @author monstaxl
 **/
public interface EmbeddedClient extends EmbeddedTerminal {

    void connect(NetworkAddress address);

    class Configuration {
        private String ip;
        private Integer port;
        private Boolean epollActivated;

    }

}
