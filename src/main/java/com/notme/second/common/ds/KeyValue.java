package com.notme.second.common.ds;

import lombok.Getter;

/**
 * @author listen
 **/
@Getter
public class KeyValue<Key, Value> {

    /* representing k,v structure */
    private final Key key;

    private final Value value;

    public KeyValue(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    public static <k, v> KeyValue<k, v> of(k key, v value) {
        return new KeyValue<>(key, value);
    }

}
