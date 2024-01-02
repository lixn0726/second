package com.notme.second.protocol;

import lombok.Data;

/**
 * @author monstaxl
 **/
@Data
public class MessageBody<T> {

    // 业务码
    private int bizCode;

    private T data;

}