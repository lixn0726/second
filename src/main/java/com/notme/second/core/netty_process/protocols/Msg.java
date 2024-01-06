package com.notme.second.core.netty_process.protocols;

import java.io.Serializable;

/**
 * @author monstaxl
 * 发送一条消息，只要知道
 *      1. 内容
 *      2. 对应的主题
 **/
public class Msg implements Serializable {

    private static final long serialVersionUID = -123456L;

    private byte magicNumber;

    private int bodyLength;

    private int tagBits;

    private byte[] body;

    private String topic;

}
