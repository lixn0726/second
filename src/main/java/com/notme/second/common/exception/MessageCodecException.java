package com.notme.second.common.exception;

import com.notme.second.common.exception.enums.CodecErrorCode;
import lombok.Getter;

/**
 * @author listen
 **/
@Getter
public class MessageCodecException extends RuntimeException {

    private final String msg;

    private final CodecErrorCode errCode;

    public static MessageCodecException processingExceptionFor(String msg) {
        return new MessageCodecException(msg, CodecErrorCode.CODEC_PROCESS_ERROR);
    }

    public static MessageCodecException preprocessingExceptionFor(String msg) {
        return new MessageCodecException(msg, CodecErrorCode.CANNOT_DO_CODEC);
    }

    private MessageCodecException(String msg, final CodecErrorCode errCode) {
        this.msg = msg;
        this.errCode = errCode;
    }

}
