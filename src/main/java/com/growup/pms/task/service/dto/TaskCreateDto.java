package com.growup.pms.task.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TaskCreateDto {

    private Long statusId;
    private Long userId;
    private String taskName;
    private String content;
    private Short sortOrder;
    private String startDate;
    private String endDate;

    @Builder
    public TaskCreateDto(Long statusId, Long userId, String taskName, String content, Short sortOrder, String startDate,
                         String endDate) {
        this.statusId = statusId;
        this.userId = userId;
        this.taskName = taskName;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
