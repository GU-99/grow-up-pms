package com.growup.pms.task.service.dto;

import java.time.LocalDate;
import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record TaskEditCommand(
        JsonNullable<Long> statusId,
        JsonNullable<String> taskName,
        JsonNullable<String> content,
        JsonNullable<Short> sortOrder,
        JsonNullable<LocalDate> startDate,
        JsonNullable<LocalDate> endDate
) {
}
