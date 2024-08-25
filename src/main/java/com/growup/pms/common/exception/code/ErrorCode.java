package com.growup.pms.common.exception.code;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ENTITY_NOT_FOUND(NOT_FOUND, "ET_001", "ENTITY가 없습니다."),
    ENTITY_ALREADY_EXIST(BAD_REQUEST, "ET_002", "ENTITY가 이미 존재합니다."),

    AUTH_AUTHENTICATION_ERROR(UNAUTHORIZED, "AT_001", "인증을 실패했습니다."),
    INVALID_REFRESH_TOKEN_ERROR(BAD_REQUEST, "AT_002", "리프레시 토큰이 유효하지 않습니다."),
    EMAIL_SENDING_ERROR(UNAUTHORIZED, "AT_003", "이메일 전송에 실패했습니다."),
    INVALID_EMAIL_VERIFICATION(BAD_REQUEST, "AT_004", "이메일 인증 번호가 일치하지 않습니다."),

    AUTHZ_ACCESS_DENIED(FORBIDDEN, "ATZ_001", "접근 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "IS_001", "문제가 발생했습니다. 나중에 다시 시도해주세요."),

    DATA_FORMAT_INVALID(BAD_REQUEST, "DF_001", "데이터 형식이 잘못되었습니다."),

    STORAGE_STORE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ST_001", "파일을 저장할 수 없습니다."),
    STORAGE_EMPTY_FILE_ERROR(BAD_REQUEST, "ST_002", "비어있는 파일입니다."),
    STORAGE_CREATE_FOLDER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ST_003", "저장할 폴더를 생성하지 못 했습니다."),

    USER_ALREADY_IN_TEAM(BAD_REQUEST, "TM_001", "사용자가 이미 팀에 존재합니다."),
    TEAM_NOT_FOUND(NOT_FOUND, "TM_002", "팀을 찾을 수 없습니다."),
    TEAM_MEMBER_NOT_FOUND(NOT_FOUND, "TM_003", "팀원을 찾을 수 없습니다."),
    UNAUTHORIZED_ROLE_ASSIGNMENT(BAD_REQUEST, "TM_004", "허용되지 않은 역할 지정입니다."),

    PROJECT_NOT_FOUND(NOT_FOUND, "PR_001", "존재하지 않는 프로젝트입니다."),
    INVALID_PROJECT(BAD_REQUEST, "PR_002", "부적절한 프로젝트입니다."),

    STATUS_NOT_FOUND(NOT_FOUND, "STS_001", "존재하지 않는 프로젝트 상태입니다."),

    TASK_NOT_FOUND(NOT_FOUND, "TS_001", "존재하지 않는 프로젝트 일정입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
