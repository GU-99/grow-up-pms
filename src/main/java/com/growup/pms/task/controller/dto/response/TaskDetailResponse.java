package com.growup.pms.task.controller.dto.response;

import static com.growup.pms.common.constant.RegexConstants.DATE_TIME_PATTERN;

import com.growup.pms.task.domain.Task;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskDetailResponse {

    private Long taskId;
    private Long statusId;
    private String userNickname;
    private String taskName;
    private String content;
    private Short sortOrder;
    private String startDate;
    private String endDate;

    @Builder
    public TaskDetailResponse(Long taskId, Long statusId, String userNickname, String taskName, String content,
                              Short sortOrder, String startDate, String endDate) {
        this.taskId = taskId;
        this.statusId = statusId;
        this.userNickname = userNickname;
        this.taskName = taskName;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static TaskDetailResponse of(Task task) {
        return TaskDetailResponse.builder()
                .taskId(task.getId())
                .statusId(task.getTaskStatus())
                .userNickname(task.getAssignee())
                .taskName(task.getName())
                .content(task.getContent())
                .sortOrder(task.getSortOrder())
                .startDate(formatDateOrNull(task.getStartDate()))
                .endDate(formatDateOrNull(task.getEndDate()))
                .build();
    }

    private static String formatDateOrNull(LocalDate localDAte) {
        return localDAte != null ? localDAte.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)) : null;
    }
}
