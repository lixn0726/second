package com.notme.second.architecture.processor;

/**
 * @author monstaxl
 **/
public class Event<T> {

    private final String traceId;

    private final T boundData;

    public Event(T boundData) {
        this.traceId = nextTraceId();
        this.boundData = boundData;
    }

    public T data() {
        return this.boundData;
    }

    public static String nextTraceId() {return "";}

    public static String trace(Event<?> event) {
        return event.traceId;
    }

}
