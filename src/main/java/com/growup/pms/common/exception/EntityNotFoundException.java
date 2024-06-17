package com.growup.pms.common.exception;

public class EntityNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
