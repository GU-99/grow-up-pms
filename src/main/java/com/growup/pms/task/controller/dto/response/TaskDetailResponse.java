package com.growup.pms.task.controller.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
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
}
