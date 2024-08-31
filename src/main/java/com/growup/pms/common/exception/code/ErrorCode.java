package com.growup.pms.common.exception.code;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 인증(Authentication) - AT
    AUTHENTICATION_FAILED(UNAUTHORIZED, "AT_001", "인증을 실패했습니다. 아이디와 비밀번호를 확인해 주세요."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "AT_002", "리프레시 토큰이 유효하지 않습니다. 다시 로그인해 주세요."),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "AT_003", "리프레시 토큰이 존재하지 않습니다."),
    EMAIL_SENDING_FAILURE(INTERNAL_SERVER_ERROR, "AT_004", "이메일 전송에 실패했습니다. 잠시 후 다시 시도해 주세요."),
    INVALID_EMAIL_VERIFICATION_CODE(BAD_REQUEST, "AT_005", "이메일 인증 번호가 일치하지 않습니다. 다시 확인해 주세요."),

    // 인가(Authorization) - ATZ
    ACCESS_DENIED(FORBIDDEN, "ATZ_001", "해당 리소스에 대한 접근 권한이 없습니다. 관리자에게 문의해 주세요."),

    // 사용자(User) - US
    USER_NOT_FOUND(NOT_FOUND, "US_001", "해당 사용자를 찾을 수 없습니다. 입력 정보를 확인해 주세요."),
    USER_ALREADY_EXISTS(CONFLICT, "US_002", "이미 등록된 사용자입니다. 다른 정보로 시도해 주세요."),

    // 역할(Role) - RL
    ROLE_NOT_FOUND(NOT_FOUND, "RL_001", "해당 역할이 존재하지 않습니다. 유효한 역할을 선택해 주세요."),

    // 검증 실패/데이터 포맷 오류(Data Format) - DF
    INVALID_DATA_FORMAT(BAD_REQUEST, "DF_001", "입력한 데이터 형식이 올바르지 않습니다. 입력 형식을 확인해 주세요."),

    // 스토리지(Storage) - ST
    FILE_STORAGE_ERROR(INTERNAL_SERVER_ERROR, "ST_001", "파일을 저장할 수 없습니다. 잠시 후 다시 시도해 주세요."),
    EMPTY_FILE_ERROR(BAD_REQUEST, "ST_002", "업로드된 파일이 비어 있습니다. 유효한 파일을 선택해 주세요."),
    FOLDER_CREATION_ERROR(INTERNAL_SERVER_ERROR, "ST_003", "저장 폴더를 생성하지 못했습니다. 시스템 관리자에게 문의해 주세요."),
    READ_FILE_ERROR(INTERNAL_SERVER_ERROR, "ST_004", "저장된 파일을 읽을 수 없습니다."),
    FILE_NOT_FOUND(NOT_FOUND, "ST_005", "파일을 찾을 수 없습니다."),

    // 팀(Team) - TM
    USER_ALREADY_IN_TEAM(BAD_REQUEST, "TM_001", "해당 사용자가 이미 팀에 속해 있습니다."),
    TEAM_NOT_FOUND(NOT_FOUND, "TM_002", "해당 팀을 찾을 수 없습니다. 팀 정보를 확인해 주세요."),
    TEAM_MEMBER_NOT_FOUND(NOT_FOUND, "TM_003", "해당 팀원을 찾을 수 없습니다. 멤버 정보를 확인해 주세요."),
    UNAUTHORIZED_ROLE_ASSIGNMENT(BAD_REQUEST, "TM_004", "권한이 없는 역할 지정입니다. 적절한 권한을 가진 사용자만 역할을 지정할 수 있습니다."),

    // 프로젝트(Project) - PR
    PROJECT_NOT_FOUND(NOT_FOUND, "PR_001", "해당 프로젝트를 찾을 수 없습니다. 프로젝트 정보를 확인해 주세요."),
    INVALID_PROJECT(BAD_REQUEST, "PR_002", "유효하지 않은 프로젝트입니다. 프로젝트 정보를 확인해 주세요."),

    // 상태(Status) - STS
    STATUS_NOT_FOUND(NOT_FOUND, "STS_001", "해당 프로젝트 상태를 찾을 수 없습니다. 유효한 상태를 선택해 주세요."),

    // 일정(Task) - TS
    TASK_NOT_FOUND(NOT_FOUND, "TS_001", "해당 프로젝트 일정을 찾을 수 없습니다. 일정 정보를 확인해 주세요."),

    // 내부 서버 에러(Internal Server Error) - IS
    APPLICATION_ERROR(INTERNAL_SERVER_ERROR, "IS_001", "문제가 발생했습니다. 잠시 후 다시 시도해 주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
