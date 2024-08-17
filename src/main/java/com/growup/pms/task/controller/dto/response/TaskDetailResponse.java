package com.growup.pms.task.controller.dto.response;

import com.growup.pms.task.domain.Task;
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
                .statusId(task.getStatus() != null ? task.getStatus().getId() : null)
                .userNickname(task.getUser() != null ? task.getUser().getUsername() : null)
                .taskName(task.getName())
                .content(task.getContent())
                .sortOrder(task.getSortOrder())
                .startDate(task.getStartDate() != null ? task.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
                .endDate(task.getEndDate() != null ? task.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
                .build();
    }
}
