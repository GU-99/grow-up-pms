package com.growup.pms.task.service.dto;

import com.growup.pms.status.domain.Status;
import com.growup.pms.task.domain.Task;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record TaskCreateCommand(
        Long statusId,
        List<Long> assigneeIds,
        String taskName,
        String content,
        Short sortOrder,
        LocalDate startDate,
        LocalDate endDate
) {
    public Task toEntity(Status status) {
        return Task.builder()
                .status(status)
                .name(taskName)
                .content(content)
                .sortOrder(sortOrder)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
