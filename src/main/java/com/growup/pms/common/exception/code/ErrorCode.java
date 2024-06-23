package com.growup.pms.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ENTITY_NOT_FOUND(ErrorCategory.ENTITY_ERROR, "001", "ENTITY가 없습니다."),
    ENTITY_ALREADY_EXIST(ErrorCategory.ENTITY_ERROR, "002", "ENTITY가 이미 존재합니다."),

    AUTH_AUTHENTICATION_ERROR(ErrorCategory.AUTHENTICATION_ERROR, "001", "인증을 실패했습니다."),

    AUTHZ_ACCESS_DENIED(ErrorCategory.AUTHORIZATION_ERROR, "001", "접근 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(ErrorCategory.INTERNAL_SERVER_ERROR, "001", "예상치 못한 서버 에러가 발생했습니다."),

    DATA_FORMAT_INVALID(ErrorCategory.DATA_FORMAT_ERROR, "001", "데이터 형식이 잘못되었습니다.");

    private final ErrorCategory errorCategory;
    private final String code;
    private final String message;
}
