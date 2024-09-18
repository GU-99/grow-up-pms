package com.growup.pms.project.controller.dto.response;

import static com.growup.pms.common.constant.RegexConstants.LOCAL_DATE_PATTERN;
import static com.growup.pms.common.constant.RegexConstants.LOCAL_DATE_TIME_PATTERN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectResponse {

    private Long projectId;
    private String projectName;
    private String content;
    private String startDate;
    private String endDate;
    private String createdAt;
    private String updatedAt;

    @Builder
    public ProjectResponse(Long projectId, String projectName, String content, LocalDate startDate, LocalDate endDate,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.content = content;
        this.startDate = formatDateOrNull(startDate);
        this.endDate = formatDateOrNull(endDate);
        this.createdAt = formatDateTimeOrNull(createdAt);
        this.updatedAt = formatDateTimeOrNull(updatedAt);
    }

    private static String formatDateOrNull(LocalDate localDate) {
        return localDate != null ? localDate.format(DateTimeFormatter.ofPattern(LOCAL_DATE_PATTERN)) : null;
    }

    private static String formatDateTimeOrNull(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN))
                : null;
    }
}
