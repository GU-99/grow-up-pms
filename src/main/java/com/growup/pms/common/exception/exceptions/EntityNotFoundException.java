package com.growup.pms.common.exception.exceptions;

public class EntityNotFoundException extends BusinessException {
    private final ErrorCode errorCode;

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
