package com.notme.second.architecture.processor;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author monstaxl
 * 消息处理器的集合，管理消息处理器
 **/
public class Processors {

    protected static final Map<Event<?>, Processor<Event<?>>> extensionRegisteredProcessors = new ConcurrentHashMap<>();

    // 打算做SPI扩展，毕竟MQ和IM或者注册中心的核心机制都差不太多，可能可以在
    // 后续进行 [通信处理模块] 的单独抽离

    public <T> void registerExtensionProcessor(Event<T> event, Processor<Event<T>> processor) {
        // 设计为不允许覆盖
//        extensionRegisteredProcessors.putIfAbsent(event, processor);
    }

}
