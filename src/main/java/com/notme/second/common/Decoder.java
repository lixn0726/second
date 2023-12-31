package com.notme.second.common;

import com.notme.second.common.exception.MessageCodecException;

/**
 * @author monstaxl
 **/
public interface Decoder<S, T> {

    T decode(S source) throws MessageCodecException;

}
