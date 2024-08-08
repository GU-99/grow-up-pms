package com.growup.pms.task.service.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TaskCreateCommand {

    private Long statusId;
    private Long userId;
    private String taskName;
    private String content;
    private Short sortOrder;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public TaskCreateCommand(Long statusId, Long userId, String taskName, String content, Short sortOrder,
                             LocalDate startDate, LocalDate endDate) {
        this.statusId = statusId;
        this.userId = userId;
        this.taskName = taskName;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
