package com.notme.second.architecture.communication.defaults.netty.codec;

import com.notme.second.common.Decoder;
import com.notme.second.common.exception.MessageCodecException;
import com.notme.second.trans.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author monstaxl
 **/
public class NettyMessageDecoder extends ByteToMessageDecoder implements Decoder<ByteBuf, Message> {

    private static final int basicLength = Message.basicLength();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        if (readableBytesLessThanBasicLength(in)) {
            in.resetReaderIndex();
            System.out.println("Cannot decode msg from " + ctx.channel().remoteAddress());
            return;
        }
        try {
            Message msg = decode(in);
            System.out.println("NettyServer decode a complete Message msg: " + msg);
            out.add(msg);
        } catch (MessageCodecException e) {
            System.out.println("Error msg: " + e.getMsg() + e.getErrCode().getDescription());
            ctx.close();
        }
    }

    @Override
    public Message decode(ByteBuf in) throws MessageCodecException {
        System.out.println("Try decode bytes to Message.");
        // 先标记一下，如果后面发现 length不够，那么reset一下，避免跳过了数据
        in.markReaderIndex();
        byte magicNumber = in.readByte();
        if (Message.notLegalMagicNumber(magicNumber)) {
            throw MessageCodecException.preprocessingExceptionFor("Income msg is not a legal msg.");
        }
        System.out.println("Legal magic number.");
        int bizCode = in.readInt();
        System.out.println("Legal biz code");
        int length = in.readInt();
        System.out.println("Legal length");
        if (readableBytesLessThanReadLength(length, in)) {
            // 不够，重置读指针，返回
            in.resetReaderIndex();
            throw MessageCodecException.preprocessingExceptionFor("Msg body is not long enough.");
        }
        System.out.println("Can decode byte to class: Message");
        byte[] body = new byte[length];
        in.readBytes(body);
        return new Message.Builder()
                .bizCode(bizCode)
                .bodyLength(length)
                .body(body)
                .build();
    }

    private boolean readableBytesLessThanBasicLength(ByteBuf in) {
        System.out.println("Current ByteBuf readable bytes: " + in.readableBytes());
        return in.readableBytes() < basicLength;
    }

    private boolean readableBytesLessThanReadLength(final int readLength, final ByteBuf in) {
        return in.readableBytes() < readLength;
    }

}
