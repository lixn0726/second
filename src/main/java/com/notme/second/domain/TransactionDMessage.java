package com.notme.second.domain;

/**
 * @author listen
 **/
public interface TransactionDMessage extends DMessage {

    String transactionId();

    boolean committed();

}
