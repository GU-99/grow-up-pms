package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class EncryptionException extends BusinessException {

    public EncryptionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
