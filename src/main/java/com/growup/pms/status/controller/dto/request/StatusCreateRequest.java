package com.growup.pms.status.controller.dto.request;

import com.growup.pms.common.util.EncryptionUtil;
import com.growup.pms.status.service.dto.StatusCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        Short sortOrder
) {
    public StatusCreateCommand toCommand(String projectId) {
        return StatusCreateCommand.builder()
                .projectId(EncryptionUtil.decrypt(projectId))
                .name(name)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .build();
    }
}
