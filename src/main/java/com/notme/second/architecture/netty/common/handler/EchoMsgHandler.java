package com.notme.second.architecture.netty.common.handler;


import com.notme.second.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author monstaxl
 **/
public class EchoMsgHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if (checkMsgType(msg)) {
            System.out.println("Received msg from:" + ctx.channel().remoteAddress().toString() + "\n" +
                    "content          :" + msg + "\n" +
                    "at               :" + System.currentTimeMillis());
        } else {
            System.err.println("Received illegal type of msg: " + msg.getClass().getName());
        }
    }

    private boolean checkMsgType(Object msg) {
        // 以A类在前面 B类在后面为例
        // 如果
        // - A和B类型一致 / A是B的父类   ---> TRUE
        // - B是A的父类                 ---> FALSE
        return Message.class.isAssignableFrom(msg.getClass());
    }

    public static void main(String[] args) {
        System.out.println(Object.class.isAssignableFrom(Object.class));
        // false
        System.out.println(String.class.isAssignableFrom(Object.class));
        // true
        System.out.println(Object.class.isAssignableFrom(String.class));
    }
}
