package com.notme.second.architecture.communication;

import com.notme.second.architecture.NetworkAddress;

/**
 * @author monstaxl
 **/
public interface EmbeddedServer extends EmbeddedTerminal {

    void bind(NetworkAddress address);

}
