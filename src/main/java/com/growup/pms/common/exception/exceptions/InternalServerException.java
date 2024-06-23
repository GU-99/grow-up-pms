package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;

public class InternalServerException extends BusinessException {

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
