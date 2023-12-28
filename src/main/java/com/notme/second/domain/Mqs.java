package com.notme.second.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author listen
 * 管理一批Mq对象，不允许重复的topic
 **/
public class Mqs {

    private final List<Mq> mqs = new CopyOnWriteArrayList<>();

    private final List<String> topics = new ArrayList<>();

    // 由于这里 duplicate argument class，后续我可能会将 Id给封装成一个类
    public static Mq fetchMqById(Mqs mqs, String id) {
        for (Mq mq : mqs.mqs) {
            if (id.equals(mq.getId())) {
                return mq;
            }
        }
        return null;
    }

    public static Mq fetchMqByTopic(Mqs mqs, String topic) {
        for (Mq mq : mqs.mqs) {
            if (topic.equals(mq.getId())) {
                return mq;
            }
        }
        return null;
    }
    
    public List<String> topics() {
        // returns a replica
        return new ArrayList<>(topics);
    }

    // only expose this
    public boolean addTopic(String topic) {
        final Mq mq = fetchMqByTopic(this, topic);
        if (Objects.isNull(mq)) {
            return addMq(Mq.withRandomId(topic));
        }
        return false;
    }

    private boolean addMq(Mq mq) {
        mqs.add(mq);
        topics.add(mq.getTopic());
        return true;
    }

    public boolean removeTopic(String topic) {
        final Mq mq = fetchMqByTopic(this, topic);
        if (Objects.isNull(mq)) {
            return false;
        }
        removeMq(mq);
        return true;
    }
    
    public void removeMq(Mq mq) {
        mqs.remove(mq);
        topics.remove(mq.getTopic());
    }

//    private boolean registeredMqOrTopic(Mq mq) {
//        return mqs.contains(mq) || topics.contains(mq.getTopic());
//    }
}
