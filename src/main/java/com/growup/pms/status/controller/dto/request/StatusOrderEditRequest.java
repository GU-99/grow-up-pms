package com.growup.pms.status.controller.dto.request;

import com.growup.pms.status.service.dto.StatusOrderEditCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record StatusOrderEditRequest(
        @Positive
        @NotNull
        Long statusId,

        @Positive
        @NotNull
        Short sortOrder
) {

    public StatusOrderEditCommand toCommand() {
        return StatusOrderEditCommand.builder()
                .statusId(statusId)
                .sortOrder(sortOrder)
                .build();
    }
}
