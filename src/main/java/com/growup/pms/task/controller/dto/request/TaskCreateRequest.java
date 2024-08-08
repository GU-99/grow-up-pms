package com.growup.pms.task.controller.dto.request;

import com.growup.pms.task.service.dto.TaskCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TaskCreateRequest {

    private Long statusId;

    @NotBlank
    @Size(max = 128)
    private String taskName;

    @NotBlank
    private String content;

    @NotNull
    @Positive
    private Short sortOrder;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public TaskCreateRequest(Long statusId, String taskName, String content, Short sortOrder, LocalDate startDate,
                             LocalDate endDate) {
        this.statusId = statusId;
        this.taskName = taskName;
        this.content = content;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }

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
