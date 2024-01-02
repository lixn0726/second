package com.notme.second.architecture.netty.codec;

import com.notme.second.architecture.netty.common.Decoder;
import com.notme.second.architecture.netty.common.exception.MessageCodecException;
import com.notme.second.protocol.Message;
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
            return;
        }
        try {
            Message msg = decode(in);
            out.add(msg);
        } catch (MessageCodecException e) {
            ctx.close();
        }
    }

    @Override
    public Message decode(ByteBuf in) throws MessageCodecException {
        // 先标记一下，如果后面发现 length不够，那么reset一下，避免跳过了数据
        in.markReaderIndex();
        byte magicNumber = in.readByte();
        if (Message.notLegalMagicNumber(magicNumber)) {
            throw MessageCodecException.preprocessingExceptionFor("Income msg is not a legal msg.");
        }
        int bizCode = in.readInt();
        int length = in.readInt();
        if (readableBytesLessThanReadLength(length, in)) {
            // 不够，重置读指针，返回
            in.resetReaderIndex();
            throw MessageCodecException.preprocessingExceptionFor("Msg body is not long enough.");
        }
        byte[] body = new byte[length];
        in.readBytes(body);
        return new Message.Builder()
                .bizCode(bizCode)
                .bodyLength(length)
                .body(body)
                .build();
    }

    private boolean readableBytesLessThanBasicLength(ByteBuf in) {
        return in.readableBytes() < basicLength;
    }

    private boolean readableBytesLessThanReadLength(final int readLength, final ByteBuf in) {
        return in.readableBytes() < readLength;
    }

}
