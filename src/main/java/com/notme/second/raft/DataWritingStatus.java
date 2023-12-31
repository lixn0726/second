package com.notme.second.raft;

/**
 * @author monstaxl
 **/
public enum DataWritingStatus {

    // 2pc commit
    UNCOMMIT,
    COMMIT,
    ;

}
