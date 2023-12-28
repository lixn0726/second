package com.notme.second.netty.controlling;

import lombok.Data;

import java.io.Serializable;

/**
 * @author listen
 **/
@Data
public final class MessageQueue implements Serializable {

    private String id;

    // 直接用一个topic绑定，而不是像RocketMQ一样还有group
    // 后续的设计，通过topic标志一个mq
    // 不同的mq，可以标记不同的tag，通过后台提供的admin platform进行消息的筛选和查询
    private String topic;

}
