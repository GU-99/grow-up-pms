package com.growup.pms.project.controller.dto.request;

import com.growup.pms.project.service.dto.ProjectUserCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProjectUserCreateRequest(
        @NotNull
        @Positive
        Long userId,
        @NotBlank
        @Size(max = 10)
        String roleName
) {
    public ProjectUserCreateCommand toCommand() {
        return ProjectUserCreateCommand.builder()
                .userId(userId)
                .roleName(roleName)
                .build();
    }
}
