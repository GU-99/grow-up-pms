package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
