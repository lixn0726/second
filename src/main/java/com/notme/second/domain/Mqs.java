package com.notme.second.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * @author monstaxl
 * 管理一批Mq对象，不允许重复的topic
 **/
public class Mqs {

    private final List<Mq> mqs = new CopyOnWriteArrayList<>();

    private final List<String> topics = new ArrayList<>();

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
                    That's called CURRYING
                    对应到Java，从Jdk1.8之后加入的Function等接口已经提供了这种能力
    Tryna understand this and change the use case of method: fetchMq( // that's todo // )
     */
    public static Mq fetchMq(Mqs mqs, Predicate<? super Mq> filterPredicate) {
        return fetchFirstMqOrdered(mqs, filterPredicate);
    }

    private static Mq fetchFirstMqOrdered(Mqs mqs, Predicate<? super Mq> filterPredicate) {
        // todo: 这里其可以再改为递归，因为按道理所有的for都可以改成递归
        return mqs.mqs.stream()
                .filter(filterPredicate)
                .findFirst()
                .orElse(null);
    }

    public List<String> topics() {
        // returns a replica
        return new ArrayList<>(topics);
    }

    public boolean addTopic(String topic) {
        final Mq mq = fetchMq(this, m -> topic.equals(m.getTopic()));
        if (Objects.isNull(mq)) {
            addMq(Mq.withRandomId(topic));
            return true;
        }
        return false;
    }

    public boolean removeTopic(String topic) {
        Mq mq = fetchMq(this, m -> topic.equals(m.getTopic()));
        if (Objects.isNull(mq)) {
            return false;
        }
        doRemoveMq(mq);
        return true;
    }

    private void addMq(Mq mq) {
        mqs.add(mq);
        topics.add(mq.getTopic());
    }

    /*
    Functional Programming：
    实体对象和函数的关系应该是：
    Object --- 绑定 --- 函数
    而不是：
    Object --- 包含 --- 函数，也就是说，并不是 函数 --- 属于 --- Object
    这和面向对象的思想貌似是不太一致的
     */
    private boolean handleTopics(String topic//, Function<?, ?> mqFunction
                                 ) {
        return true;
    }
    
    public void doRemoveMq(Mq mq) {
        topics.remove(mq.getTopic());
        mqs.remove(mq);
    }

//    private boolean registeredMqOrTopic(Mq mq) {
//        return mqs.contains(mq) || topics.contains(mq.getTopic());
//    }
}
