package com.growup.pms.task.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record TaskOrderListEditRequest(@Valid @NotNull List<TaskOrderEditRequest> tasks) {

}
