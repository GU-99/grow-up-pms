package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;

public class InvalidProjectException extends BusinessException {

    public InvalidProjectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
