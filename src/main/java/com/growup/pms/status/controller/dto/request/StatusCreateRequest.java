package com.growup.pms.status.controller.dto.request;

import com.growup.pms.status.service.dto.StatusCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusCreateRequest {

    @NotBlank
    @Size(min = 1, max = 32)
    private String name;

    @NotBlank
    @Size(min = 1, max = 6)
    private String colorCode;

    @NotNull
    private Short sortOrder;

    @Builder
    public StatusCreateRequest(String name, String colorCode, Short sortOrder) {
        this.name = name;
        this.colorCode = colorCode;
        this.sortOrder = sortOrder;
    }

    public StatusCreateCommand toCommand(Long projectId) {
        return StatusCreateCommand.builder()
                .projectId(projectId)
                .name(name)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .build();
    }
}
