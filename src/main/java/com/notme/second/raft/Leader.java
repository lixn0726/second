package com.notme.second.raft;

/**
 * @author listen
 * 某个节点得到了大多数集群节点的投票，从Candidate变更为Leader
 * Leader才可以处理客户端数据的请求，非Leader节点接收到请求会转发给Leader
 **/
public class Leader {
}
