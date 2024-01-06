package com.notme.second.core.netty_process.protocols;

/**
 * @author monstaxl
 **/
public interface CommandHeader {

    CommandHeader build();

    String get(String key);

    void set(String key, String value);

}
