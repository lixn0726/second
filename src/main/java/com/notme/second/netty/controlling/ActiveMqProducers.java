package com.notme.second.netty.controlling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author monstaxl
 **/
public class ActiveMqProducers {

    private final Map<String, Peer> mqProducers = new ConcurrentHashMap<>();

}
