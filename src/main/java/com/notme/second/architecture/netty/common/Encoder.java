package com.notme.second.architecture.netty.common;

import com.notme.second.architecture.netty.common.exception.MessageCodecException;

/**
 * @author monstaxl
 **/
public interface Encoder<T, S> {

    T encode(S source) throws MessageCodecException;

}
