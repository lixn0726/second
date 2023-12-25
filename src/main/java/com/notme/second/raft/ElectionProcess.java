package com.notme.second.raft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class ElectionProcess {

    private static final Logger log = LoggerFactory.getLogger(ElectionProcess.class);

    private String clusterId;

    private Endpoint node;

    // 150 ~ 300ms
    private long randomElectionTimeout;

    private void doElection() {
        waitForRandomElectionTimeout();
        afterRandomTimeoutPassed();
    }

    private void waitForRandomElectionTimeout() {
        try {
            TimeUnit.MILLISECONDS.sleep(randomElectionTimeout);
        } catch (Exception e) {
            log.error("Endpoint [{}] wait for [{}] ms before election error.", node.id(), randomElectionTimeout);
        }
    }

    private void afterRandomTimeoutPassed() {
        voteToMyself();
        advanceMyselfToCandidate();
        voteToOtherEndpointInTheSameCluster();
        getVoteResult();
        advanceToLeaderIfGetMoreThanHalfVote();
    }

    private void voteToMyself() {}

    private void advanceMyselfToCandidate() {}

    private void voteToOtherEndpointInTheSameCluster() {}

    private void getVoteResult() {}

    private void advanceToLeaderIfGetMoreThanHalfVote() {}

}
