package com.growup.pms.task.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.growup.pms.task.service.dto.TaskCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TaskCreateRequest(
        Long statusId,

        Long userId,

        @NotBlank
        @Size(max = 128)
        String taskName,

        @NotBlank
        String content,

        @NotNull
        @Positive
        Short sortOrder,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
    public TaskCreateCommand toCommand() {
        return TaskCreateCommand.builder()
                .userId(userId)
                .statusId(statusId)
                .taskName(taskName)
                .content(content)
                .sortOrder(sortOrder)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
