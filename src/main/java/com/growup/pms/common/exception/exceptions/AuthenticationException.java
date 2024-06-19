package com.growup.pms.common.exception.exceptions;

import lombok.Getter;

@Getter
public class AuthenticationException extends BusinessException {
    private final ErrorCode errorCode;

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
