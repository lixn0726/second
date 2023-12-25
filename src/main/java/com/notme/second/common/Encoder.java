package com.notme.second.common;

import com.notme.second.common.exception.MessageCodecException;

/**
 * @author listen
 **/
public interface Encoder<T, S> {

    T encode(S source) throws MessageCodecException;

}
