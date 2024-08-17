package com.growup.pms.task.service.dto;

import com.growup.pms.status.domain.Status;
import com.growup.pms.task.domain.Task;
import com.growup.pms.user.domain.User;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TaskCreateCommand(
        Long statusId,
        Long userId,
        String taskName,
        String content,
        Short sortOrder,
        LocalDate startDate,
        LocalDate endDate
) {
    public Task toEntity(Status status, User user) {
        return Task.builder()
                .status(status)
                .user(user)
                .name(taskName)
                .content(content)
                .sortOrder(sortOrder)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
