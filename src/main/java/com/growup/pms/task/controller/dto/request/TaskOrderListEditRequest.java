package com.growup.pms.task.controller.dto.request;

import com.growup.pms.task.service.dto.TaskOrderEditCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record TaskOrderListEditRequest(@Valid @NotNull List<TaskOrderEditRequest> tasks) {

    public List<TaskOrderEditCommand> toCommands() {
        return tasks.stream().map(TaskOrderEditRequest::toCommand).toList();
    }
}
