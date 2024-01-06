package com.notme.second.naming;

/**
 * @author monstaxl
 **/
public interface BrokerSelector {

    // load from db.
    String getBroker(BrokerSelectionStrategy strategy);

}
