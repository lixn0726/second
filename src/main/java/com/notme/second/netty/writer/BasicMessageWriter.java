package com.notme.second.netty.writer;

import com.notme.second.trans.Message;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author listen
 **/
public abstract class BasicMessageWriter<T> implements MessageWriter<T> {

    protected List<Field> allFieldsOfMessage(Message msg) {
        return Arrays.asList(Message.class.getDeclaredFields());
    }

}
