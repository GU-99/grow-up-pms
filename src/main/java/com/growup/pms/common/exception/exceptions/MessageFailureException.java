package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;

public class MessageFailureException extends BusinessException {

    public MessageFailureException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MessageFailureException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
