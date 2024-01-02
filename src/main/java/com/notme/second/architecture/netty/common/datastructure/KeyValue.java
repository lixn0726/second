package com.notme.second.architecture.netty.common.datastructure;

import lombok.Getter;

/**
 * @author monstaxl
 **/
@Getter
public class KeyValue<Key, Value> {

    /* representing k,v structure */
    private final Key key;

    private final Value value;

    public KeyValue(Key key, Value value) {
        this.key = key;
        this.value = value;
//        MappedByteBuffer mmap = FileChannel.open().map();
    }

    public static <k, v> KeyValue<k, v> of(k key, v value) {
        return new KeyValue<>(key, value);
    }

}
