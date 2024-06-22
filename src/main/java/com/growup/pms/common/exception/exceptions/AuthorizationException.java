package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;

public class AuthorizationException extends BusinessException {
    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
