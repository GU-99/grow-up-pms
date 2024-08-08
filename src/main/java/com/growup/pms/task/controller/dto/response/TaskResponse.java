package com.growup.pms.task.controller.dto.response;

import lombok.Builder;

@Builder
public record TaskResponse(
        Long taskId,
        Long statusId,
        String taskName,
        String userNickname,
        Short sortOrder
) {
}
