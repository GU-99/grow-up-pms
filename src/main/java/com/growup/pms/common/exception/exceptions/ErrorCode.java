package com.growup.pms.common.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "ET_001", "Entity error: ENTITY가 없습니다."),

    AUTH_AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "AT_001","Authentication error : 인증을 실패했습니다."),

    AUTHZ_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ATZ_001","Authorization error : 접근권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
