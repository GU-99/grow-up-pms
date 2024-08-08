package com.growup.pms.task.service.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TaskCreateCommand(
        Long statusId,
        String taskName,
        String content,
        Short sortOrder,
        LocalDate startDate,
        LocalDate endDate
) {
}
