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
public class NettyMessageEncoder extends MessageToByteEncoder<Message>
//        implements Encoder<ByteBuf, Message>
{

    /*
    这里需要注意的是，如果自定义了Encoder，比如这里我的Encoder指定的类型为Message
    如果 channel调用 writeAndFlush发送其他类型的数据，那么是会发送不出去的
    并且必须要在 writeAndFlush加上 Listener对发送结果进行监听，才能从 cause中知道是类型错误，而不是直接抛出异常
    这里我个人感觉是 Netty的一个坑，但是这也是 Future的设计，只能说自己可能对 api的使用还不是很熟练
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) {
        if (cannotWriteToBuffer(msg, out)) {
            return;
        }
        encodeAndWrite(msg, out);
    }

    private void encodeAndWrite(final Message msg, final ByteBuf out) {
        out.writeByte(msg.getMagicNumber());
        out.writeInt(msg.getBizCode());
        out.writeInt(msg.getBodyLength());
        out.writeBytes(msg.getBody());
    }

    private boolean cannotWriteToBuffer(Message msg, ByteBuf out) {
        return !canWriteToBuffer(msg, out);
    }

    private boolean canWriteToBuffer(Message msg, ByteBuf out) {
        return out.isWritable(Message.requireBufferSizeToWrite(msg));
    }

    // not allow to direct call encode()
//    @Override
//    public ByteBuf encode(Message source) throws MessageCodecException {
//        throw MessageCodecException.processingExceptionFor("Cannot directly encode msg to class Netty#ByteBuf");
//        throw new UnsupportedOperationException();
//    }
}
