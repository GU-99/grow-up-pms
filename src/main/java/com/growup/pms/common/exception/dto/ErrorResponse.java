package com.growup.pms.common.exception.dto;

import com.growup.pms.common.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String code;
    private String message;

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        String code = errorCode.getErrorCategory().getCodePrefix() + errorCode.getCode();
        String message = errorCode.getErrorCategory().getMessagePrefix() + errorCode.getMessage();
        return new ErrorResponse(code, message);
    }
}
