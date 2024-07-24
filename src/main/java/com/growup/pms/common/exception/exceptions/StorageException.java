package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;

public class StorageException extends BusinessException {

    public StorageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
