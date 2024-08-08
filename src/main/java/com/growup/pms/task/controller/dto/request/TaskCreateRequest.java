package com.growup.pms.task.controller.dto.request;

import com.growup.pms.task.service.dto.TaskCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record TaskCreateRequest(
        Long statusId,

        @NotBlank
        @Size(max = 128)
        String taskName,

        @NotBlank
        String content,

        @NotNull
        @Positive
        Short sortOrder,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
    public TaskCreateCommand toCommand(Long userId) {
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
