package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.dto.ErrorDto;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final transient ErrorDto errorDto;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorDto = ErrorDto.builder().
                httpStatus(errorCode.getHttpStatus()).
                message(errorCode.getMessage()).
                code(errorCode.getCode()).
                build();
    }
}
