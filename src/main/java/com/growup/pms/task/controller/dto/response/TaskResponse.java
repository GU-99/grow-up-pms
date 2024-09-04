package com.growup.pms.task.controller.dto.response;

import lombok.Builder;

@Builder
public record TaskResponse(
        // TODO: 담당자 목록을 포함하도록 수정 & 새로 픽스된 API 명세서와 동일한 필드로 수정
        Long taskId,
        Long statusId,
        String taskName,
        Short sortOrder
) {
}
