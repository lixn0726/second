package com.notme.second.architecture.processor;

/**
 * @author monstaxl
 * todo 这么设计明显是不太好的
 **/
public interface Processor<T> {

    Event<T> interestingEvent();

    void process(Event<T> event);

}
