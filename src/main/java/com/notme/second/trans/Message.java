package com.notme.second.trans;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author listen
 **/
@Getter
@Setter
public class Message implements Serializable {

    private static final long serialVersionUID = -12345654321L;

    /// -=-=-=-=-=-=-=-=-= 726 = 15 = f =-=-=-=-=-=-=-=-=- ///
    private static final byte NOTME_MAGIC = 0xf;

    // 魔数
    private byte magicNumber;

    // 消息长度，说白了就是期望的body长度
    private int bodyLength;

    private int bizCode;

    // 序列化后的消息
    private byte[] body;

    private Message() {
    }

    public static boolean notLegalMagicNumber(final int income) {
        return !isLegalMagicNumber(income);
    }

    public static boolean isLegalMagicNumber(final int income) {
        return income == NOTME_MAGIC;
    }

    public static int basicLength() {
        return 1 + 4 + 4;
    }

    public static int requireBufferSizeToWrite(Message msg) {
        return basicLength() + msg.getBody().length;
    }

    public static class Builder {

        private final Message msg;

        public Builder() {
            this.msg = new Message();
            msg.setMagicNumber(NOTME_MAGIC);
        }

        public Builder magicNumber(final byte magicNumber) {
            if (notLegalMagicNumber(magicNumber)) {
                throw new IllegalArgumentException("Error creating Message with not right field value: " +
                        "magicNumber = {" + magicNumber + "}");
            }
            msg.setMagicNumber(magicNumber);
            return this;
        }

        public Builder bodyLength(final int length) {
            if (length <= 0) {
                throw new IllegalArgumentException("Error creating Message with not right field value: " +
                        "length = {" + length + "}");
            }
            msg.setBodyLength(length);
            return this;
        }

        public Builder bizCode(final int bizCode) {
            msg.setBizCode(bizCode);
            return this;
        }

        public Builder body(final byte[] body) {
            msg.setBody(body);
            return this;
        }

        public Message build() {
            return msg;
        }

    }

    @Override
    public String toString() {
        return "Message{" +
                "magicNumber=" + magicNumber +
                ", bodyLength=" + bodyLength +
                ", bizCode=" + bizCode +
                ", body(String Version)= [" + new String(body, StandardCharsets.UTF_8) +
                "] }";
    }
}
