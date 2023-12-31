package com.notme.second.domain;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TopLevelAttribute;

/**
 * @author monstaxl
 **/
public class Mq {

    private final String id;

    private final String topic;

    public static Mq withRandomId(String topic) {
        return new Mq(topic);
    }

    public Mq(String topic) {
        this(generateId(), topic);
    }

    private static String generateId() {
        return "id";
    }

    private Mq(String id, String topic) {
        this.id = id;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }
}
