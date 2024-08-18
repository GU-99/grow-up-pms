package com.growup.pms.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCategory {
    ENTITY_ERROR("ET_", "Entity error: "),
    AUTHENTICATION_ERROR("AT_", "Authentication error: "),
    AUTHORIZATION_ERROR("ATZ_", "Authorization error: "),
    INTERNAL_SERVER_ERROR("IS_", "Internal server error: "),
    DATA_FORMAT_ERROR("DF_", "Data format error: "),
    STORAGE_ERROR("ST_", "Storage error: "),
    TEAM_ERROR("TM_", "Team error: "),
    PROJECT_ERROR("PR_", "Project error: "),
    STATUS_ERROR("STS_", "Status error: "),
    TASK_ERROR("TS_", "Task error: ");

    private final String codePrefix;
    private final String messagePrefix;
}
