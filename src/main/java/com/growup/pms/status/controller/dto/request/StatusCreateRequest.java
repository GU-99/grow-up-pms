package com.growup.pms.status.controller.dto.request;

import com.growup.pms.status.service.dto.StatusCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record StatusCreateRequest(
        @NotBlank
        @Size(min = 1, max = 32)
        String name,

        @NotBlank
        @Size(min = 1, max = 6)
        String colorCode,

        @NotNull
        @PositiveOrZero
        Short sortOrder
) {
    public StatusCreateCommand toCommand(Long projectId) {
        return StatusCreateCommand.builder()
                .projectId(projectId)
                .name(name)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .build();
    }
}
