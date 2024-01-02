package com.notme.second.architecture.netty.common;

import com.notme.second.architecture.netty.common.exception.MessageCodecException;

/**
 * @author monstaxl
 **/
public interface Decoder<S, T> {

    T decode(S source) throws MessageCodecException;

}
