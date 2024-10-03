package com.growup.pms.task.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TaskUserCreateRequest(@Positive @NotNull Long userId) {

}
