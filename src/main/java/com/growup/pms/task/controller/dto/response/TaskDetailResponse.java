package com.growup.pms.task.controller.dto.response;

import lombok.Builder;

@Builder
public record TaskDetailResponse(
        Long taskId,
        Long statusId,
        String userNickname,
        String taskName,
        String content,
        Short sortOrder,
        String startDate,
        String endDate
) {
}
