package com.growup.pms.task.controller.dto.response;

import static com.growup.pms.common.constant.RegexConstants.LOCAL_DATE_PATTERN;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskResponse {

    private Long taskId;
    private Long statusId;
    private String taskName;
    private String content;
    private Short sortOrder;
    private String startDate;
    private String endDate;

    @Builder
    public TaskResponse(Long taskId, Long statusId, String taskName, String content, Short sortOrder, LocalDate startDate,
                        LocalDate endDate) {
        this.taskId = taskId;
        this.statusId = statusId;
        this.taskName = taskName;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = formatDateOrNull(startDate);
        this.endDate = formatDateOrNull(endDate);
    }

    private static String formatDateOrNull(LocalDate localDate) {
        return localDate != null ? localDate.format(DateTimeFormatter.ofPattern(LOCAL_DATE_PATTERN)) : null;
    }
}
