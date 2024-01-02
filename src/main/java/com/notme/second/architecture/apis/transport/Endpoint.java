package com.notme.second.architecture.apis.transport;

import com.notme.second.architecture.NetworkAddress;

/**
 * @author monstaxl
 * 抽象一个节点
 **/
public interface Endpoint {

    String id();

    NetworkAddress address();

    void start();

    void shutdown();

}
