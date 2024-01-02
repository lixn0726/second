package com.notme.second.architecture.processor;

/**
 * @author monstaxl
 **/
public abstract class AbstractProcessor<T> implements Processor<T> {

    private final Event<T> interstingEvent;

    public AbstractProcessor(Event<T> interstingEvent) {
        this.interstingEvent = interstingEvent;
    }

    @Override
    public Event<T> interestingEvent() {
        return this.interstingEvent;
    }

}
