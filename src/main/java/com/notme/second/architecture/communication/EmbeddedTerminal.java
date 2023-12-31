package com.notme.second.architecture.communication;

import com.notme.second.architecture.NetworkAddress;

/**
 * @author monstaxl
 * 内嵌的终端，作为SDK使用
 **/
public interface EmbeddedTerminal {

    NetworkAddress address();

    void setAddress(NetworkAddress address);

    void start();

    void shutdown();

}
