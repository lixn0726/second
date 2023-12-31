package com.notme.second.domain.processor;

import com.notme.second.domain.event.Event;

/**
 * @author monstaxl
 **/
public interface Processor {

    Event interestingEvent();

    void process(Event event);

}
