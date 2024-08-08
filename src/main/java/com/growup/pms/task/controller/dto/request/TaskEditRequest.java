package com.growup.pms.task.controller.dto.request;

import com.growup.pms.task.service.dto.TaskEditCommand;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TaskEditRequest {

    private JsonNullable<Long> statusId = JsonNullable.undefined();

    @Size(max = 128)
    private JsonNullable<String> taskName = JsonNullable.undefined();

    private JsonNullable<String> content = JsonNullable.undefined();

    @Positive
    private JsonNullable<Short> sortOrder = JsonNullable.undefined();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private JsonNullable<LocalDate> startDate = JsonNullable.undefined();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private JsonNullable<LocalDate> endDate = JsonNullable.undefined();

    @Builder
    public TaskEditRequest(JsonNullable<Long> statusId, JsonNullable<String> taskName, JsonNullable<String> content,
                           JsonNullable<Short> sortOrder, JsonNullable<LocalDate> startDate,
                           JsonNullable<LocalDate> endDate) {
        this.statusId = statusId;
        this.taskName = taskName;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TaskEditCommand toCommand(Long userId) {
        return TaskEditCommand.builder()
                .userId(userId)
                .statusId(statusId)
                .content(content)
                .taskName(taskName)
                .sortOrder(sortOrder)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
