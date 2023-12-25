package com.notme.second.trans;

import lombok.Data;

/**
 * @author listen
 **/
@Data
public class MessageBody<T> {

    // 业务码
    private int bizCode;

    private T data;

}
