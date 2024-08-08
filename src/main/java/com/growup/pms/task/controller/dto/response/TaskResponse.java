package com.growup.pms.task.controller.dto.response;

import com.growup.pms.common.util.EncryptionUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskResponse {

    private String taskId;
    private String statusId;
    private String taskName;
    private String userNickname;
    private Short sortOrder;

    @Builder
    public TaskResponse(Long taskId, Long statusId, String taskName, String userNickname, Short sortOrder) {
        this.taskId = EncryptionUtil.encrypt(String.valueOf(taskId));
        this.statusId = EncryptionUtil.encrypt(String.valueOf(statusId));
        this.taskName = taskName;
        this.userNickname = userNickname;
        this.sortOrder = sortOrder;
    }
}
