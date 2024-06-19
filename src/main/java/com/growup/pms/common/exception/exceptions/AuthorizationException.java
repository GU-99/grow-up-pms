package com.growup.pms.common.exception.exceptions;

public class AuthorizationException extends BusinessException {
    private final ErrorCode errorCode;

    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
