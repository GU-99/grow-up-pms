package com.growup.pms.common.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
