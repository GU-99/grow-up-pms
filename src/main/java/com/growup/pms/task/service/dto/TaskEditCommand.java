package com.growup.pms.task.service.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TaskEditCommand {

    private Long userId;

    private JsonNullable<Long> statusId;

    private JsonNullable<String> taskName;

    private JsonNullable<String> content;

    private JsonNullable<Short> sortOrder;

    private JsonNullable<LocalDate> startDate;

    private JsonNullable<LocalDate> endDate;

    @Builder
    public TaskEditCommand(Long userId, JsonNullable<Long> statusId, JsonNullable<String> taskName,
                           JsonNullable<String> content, JsonNullable<Short> sortOrder, JsonNullable<LocalDate> startDate,
                           JsonNullable<LocalDate> endDate) {
        this.userId = userId;
        this.statusId = statusId;
        this.taskName = taskName;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
