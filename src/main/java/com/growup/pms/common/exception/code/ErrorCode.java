package com.growup.pms.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ENTITY_NOT_FOUND(ErrorCategory.ENTITY_ERROR, "001", "ENTITY가 없습니다."),
    ENTITY_ALREADY_EXIST(ErrorCategory.ENTITY_ERROR, "002", "ENTITY가 이미 존재합니다."),

    AUTH_AUTHENTICATION_ERROR(ErrorCategory.AUTHENTICATION_ERROR, "001", "인증을 실패했습니다."),
    INVALID_REFRESH_TOKEN_ERROR(ErrorCategory.AUTHENTICATION_ERROR, "002", "리프레시 토큰이 유효하지 않습니다."),

    AUTHZ_ACCESS_DENIED(ErrorCategory.AUTHORIZATION_ERROR, "001", "접근 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(ErrorCategory.INTERNAL_SERVER_ERROR, "001", "예상치 못한 서버 에러가 발생했습니다."),

    DATA_FORMAT_INVALID(ErrorCategory.DATA_FORMAT_ERROR, "001", "데이터 형식이 잘못되었습니다."),

    STORAGE_STORE_ERROR(ErrorCategory.STORAGE_ERROR, "001", "파일을 저장할 수 없습니다."),
    STORAGE_EMPTY_FILE_ERROR(ErrorCategory.STORAGE_ERROR, "002", "비어있는 파일입니다."),
    STORAGE_CREATE_FOLDER_ERROR(ErrorCategory.STORAGE_ERROR, "003", "저장할 폴더를 생성하지 못 했습니다."),

    USER_ALREADY_IN_TEAM(ErrorCategory.TEAM_ERROR, "001", "사용자가 이미 팀에 존재합니다."),
    TEAM_NOT_FOUND(ErrorCategory.TEAM_ERROR, "002", "팀을 찾을 수 없습니다."),

    PROJECT_NOT_FOUND(ErrorCategory.PROJECT_ERROR, "001", "존재하지 않는 프로젝트입니다."),

    STATUS_NOT_FOUND(ErrorCategory.STATUS_ERROR, "001", "존재하지 않는 프로젝트 상태입니다.");

    private final ErrorCategory errorCategory;
    private final String code;
    private final String message;
}
