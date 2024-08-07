package com.growup.pms.task.controller.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TaskResponse {

    // 일정 PK, 상태 PK, 수행 회원 이름, 순서
    private Long taskId;
    private Long statusId;
    private String taskName;
    private String userNickname;
    private Short sortOrder;

    @Builder
    public TaskResponse(Long taskId, Long statusId, String taskName, String userNickname, Short sortOrder) {
        this.taskId = taskId;
        this.statusId = statusId;
        this.taskName = taskName;
        this.userNickname = userNickname;
        this.sortOrder = sortOrder;
    }
}
