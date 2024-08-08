package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class DecryptionException extends BusinessException {

    public DecryptionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
