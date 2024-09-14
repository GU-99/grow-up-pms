package com.growup.pms.task.controller.dto.response;

import com.growup.pms.status.domain.Status;
import java.util.List;
import lombok.Builder;

@Builder
public record TaskKanbanResponse(
        Long statusId,
        String statusName,
        String colorCode,
        Short sortOrder,
        List<TaskResponse> tasks
) {
    public static TaskKanbanResponse of(Status status, List<TaskResponse> tasks) {
        return TaskKanbanResponse.builder()
                .statusId(status.getId())
                .statusName(status.getName())
                .colorCode(status.getColorCode())
                .sortOrder(status.getSortOrder())
                .tasks(tasks)
                .build();
    }
}
