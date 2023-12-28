package com.notme.second.netty.simple;

import com.notme.second.trans.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author listen
 **/
public class ShowMsgHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if (checkMsgType(msg)) {
            System.out.println("Received msg from:" + ctx.channel().remoteAddress().toString() + "\n" +
                    "          content:" + msg + "\n" +
                    "               at:" + System.currentTimeMillis());
        } else {
            System.err.println("Received illegal type of msg: " + msg.getClass().getName());
        }
    }

    private boolean checkMsgType(Object msg) {
        System.out.println("Current receiving msg type: " + msg.getClass());
        return Message.class.equals(msg.getClass());
    }

}
