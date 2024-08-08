package com.growup.pms.task.controller.dto.response;

import com.growup.pms.common.util.EncryptionUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskDetailResponse {

    private String taskId;
    private String statusId;
    private String userNickname;
    private String taskName;
    private String content;
    private Short sortOrder;
    private String startDate;
    private String endDate;

    @Builder
    public TaskDetailResponse(Long taskId, Long statusId, String userNickname, String taskName, String content,
                              Short sortOrder, String startDate, String endDate) {
        this.taskId = EncryptionUtil.encrypt(String.valueOf(taskId));
        this.statusId = EncryptionUtil.encrypt(String.valueOf(statusId));
        this.userNickname = userNickname;
        this.taskName = taskName;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
