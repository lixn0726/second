package com.notme.second.common.exception.enums;

import lombok.Getter;

/**
 * @author listen
 **/
@Getter
public enum CodecErrorCode {

    CANNOT_DO_CODEC("Cannot process codec."),
    CODEC_PROCESS_ERROR("Codec processing error or fault occur."),
    ;

    private final String description;

    CodecErrorCode(String description) {
        this.description = description;
    }
}
