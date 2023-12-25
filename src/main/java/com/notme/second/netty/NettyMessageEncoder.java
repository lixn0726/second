package com.notme.second.netty;

import com.notme.second.common.Encoder;
import com.notme.second.common.exception.MessageCodecException;
import com.notme.second.trans.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author listen
 **/
public class NettyMessageEncoder extends MessageToByteEncoder<Message> implements Encoder<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if (cannotWriteToBuffer(msg, out)) {
            return;
        }
        encodeAndWrite(msg, out);
    }

    private void encodeAndWrite(final Message msg, final ByteBuf out) {
        out.writeByte(msg.getMagicNumber());
        out.writeInt(msg.getBodyLength());
        out.writeInt(msg.getBizCode());
        out.writeBytes(msg.getBody());
    }

    private boolean cannotWriteToBuffer(Message msg, ByteBuf out) {
        return !canWriteToBuffer(msg, out);
    }

    private boolean canWriteToBuffer(Message msg, ByteBuf out) {
        return out.isWritable(Message.requireBufferSizeToWrite(msg));
    }

    @Override
    public ByteBuf encode(Message source) throws MessageCodecException {
        throw new MessageCodecException();
    }
}
