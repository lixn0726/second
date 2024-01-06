package com.notme.second.core.netty_process.protocols;

/**
 * @author monstaxl
 **/
public interface IdGenerator<T> {

    Id<T> nextId();

}
