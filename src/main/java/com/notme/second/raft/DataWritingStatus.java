package com.notme.second.raft;

/**
 * @author listen
 **/
public enum DataWritingStatus {

    // 2pc commit
    UNCOMMIT,
    COMMIT,
    ;

}
