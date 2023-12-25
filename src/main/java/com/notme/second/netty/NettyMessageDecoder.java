package com.notme.second.netty;

import com.notme.second.common.Decoder;
import com.notme.second.common.exception.MessageCodecException;
import com.notme.second.trans.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author listen
 **/
public class NettyMessageDecoder extends ByteToMessageDecoder implements Decoder<ByteBuf, Message> {

    private static final int basicLength = Message.basicLength();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
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
            throw new MessageCodecException();
        }
        int length = in.readInt();
        if (readableBytesLessThanReadLength(length, in)) {
            // 不够，重置读指针，返回
            // todo 这里不同的情况返回不同code的Exception
            in.resetReaderIndex();
            throw new MessageCodecException();
        }
        byte[] body = new byte[length];
        in.readBytes(body);
        return new Message.Builder()
                .bodyLength(length)
                .body(body)
                .build();
    }

    private boolean readableBytesLessThanBasicLength(ByteBuf in) {
        return in.readableBytes() >= basicLength;
    }

    private boolean readableBytesLessThanReadLength(final int readLength, final ByteBuf in) {
        return in.readableBytes() < readLength;
    }

}
