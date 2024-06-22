package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.dto.ErrorResponse;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final transient ErrorResponse errorResponse; // 일반적으로 예외 처리를 위해 사용되므로 직렬화 로직을 구현할 필요가 없음

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorResponse = ErrorResponse.of(errorCode);
    }
}
