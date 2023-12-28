package com.notme.second.domain.processor;

import com.notme.second.domain.event.Event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author listen
 * 消息处理器的集合，管理消息处理器
 **/
public class Processors {

    protected static final Map<Event, Processor> extensionRegisteredProcessors = new ConcurrentHashMap<>();

    // 系统默认的消息以及处理器，因为我打算做SPI扩展，毕竟MQ和IM或者注册中心的核心机制都差不太多，可能可以在
    // 后续进行 [通信处理模块] 的单独抽离
    protected static final Map<Event, Processor> defaultProcessors = new ConcurrentHashMap<>();

    public void registerExtensionProcessor(Event event, Processor processor) {
        // 设计为不允许覆盖
        extensionRegisteredProcessors.putIfAbsent(event, processor);
    }

    public void registerDefaultProcessor(Event event, Processor processor) {
        defaultProcessors.putIfAbsent(event, processor);
    }

}
