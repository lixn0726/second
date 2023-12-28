package com.notme.second.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * @author listen
 * 管理一批Mq对象，不允许重复的topic
 **/
public class Mqs {

    private final List<Mq> mqs = new CopyOnWriteArrayList<>();

    private final List<String> topics = new ArrayList<>();

    static class ChildMq extends Mq {

        public ChildMq(String topic) {
            super(topic);
        }

        public String getChildId() { return ""; }
    }

    public static void main(String[] args) {
        Mqs mqs = new Mqs();
    }

    /*
    参考 Code Aesthetic - Dear Function
            PECS: Producer Extends, Consumer Super
            Arg: filterPredicate is a consumer, so not suitable for a Extends
    todo: what about a Function creates a Function.
            Like:  function userIdEquals(userId) {
                        return function(receipt) {
                            return receipt.userId == userId;
                        }
                    }
    Tryna understand this and change the use case of method: fetchMq( // that's todo // )
     */
    public static Mq fetchMq(Mqs mqs, Predicate<? super Mq> filterPredicate) {
        return fetchFirstMqOrdered(mqs, filterPredicate);
    }

    private static Mq fetchFirstMqOrdered(Mqs mqs, Predicate<? super Mq> filterPredicate) {
        for (Mq mq : mqs.mqs) {
            if (filterPredicate.test(mq)) {
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
        final Mq mq = fetchMq(this, m -> topic.equals(m.getTopic()));
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
        final Mq mq = fetchMq(this, m -> topic.equals(m.getTopic()));
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
