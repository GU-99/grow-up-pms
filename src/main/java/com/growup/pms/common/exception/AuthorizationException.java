package com.growup.pms.common.exception;

public class AuthorizationException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
