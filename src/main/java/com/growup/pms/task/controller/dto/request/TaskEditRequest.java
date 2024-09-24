package com.growup.pms.task.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.LOCAL_DATE_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.growup.pms.task.service.dto.TaskEditCommand;
import jakarta.validation.constraints.Size;
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
public class TaskEditRequest {

    private JsonNullable<Long> statusId = JsonNullable.undefined();

    @Size(max = 128)
    private JsonNullable<String> taskName = JsonNullable.undefined();

    private JsonNullable<String> content = JsonNullable.undefined();

    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    private JsonNullable<LocalDate> startDate = JsonNullable.undefined();

    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    private JsonNullable<LocalDate> endDate = JsonNullable.undefined();

    @Builder
    public TaskEditRequest(JsonNullable<Long> statusId, JsonNullable<String> taskName,
                           JsonNullable<String> content, JsonNullable<LocalDate> startDate,
                           JsonNullable<LocalDate> endDate) {
        this.statusId = statusId;
        this.taskName = taskName;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TaskEditCommand toCommand() {
        return TaskEditCommand.builder()
                .statusId(statusId)
                .content(content)
                .taskName(taskName)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
