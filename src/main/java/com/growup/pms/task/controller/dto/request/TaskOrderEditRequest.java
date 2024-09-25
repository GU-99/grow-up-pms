package com.growup.pms.task.controller.dto.request;

import com.growup.pms.task.service.dto.TaskOrderEditCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TaskOrderEditRequest(
        @Positive
        @NotNull
        Long statusId,

        @Positive
        @NotNull
        Long taskId,

        @Positive
        @NotNull
        Short sortOrder
) {
    public TaskOrderEditCommand toCommand() {
        return TaskOrderEditCommand.builder()
                .statusId(statusId)
                .taskId(taskId)
                .sortOrder(sortOrder)
                .build();
    }
}
