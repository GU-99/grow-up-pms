package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;

public class DuplicateException extends BusinessException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
