package com.notme.second.netty.writer;

import com.notme.second.trans.Message;

/**
 * @author listen
 **/
public interface MessageWriter<T> {

    void writeMsgTo(Message msg, T target);

}
