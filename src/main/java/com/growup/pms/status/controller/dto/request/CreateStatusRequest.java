package com.growup.pms.status.controller.dto.request;

import com.growup.pms.status.service.dto.CreateStatusDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateStatusRequest {

    @NotNull
    @Positive
    private Long projectId;

    @NotBlank
    @Size(min = 1, max = 32)
    private String name;

    @NotBlank
    @Size(min = 1, max = 6)
    private String colorCode;

    @NotNull
    @Positive
    private Short sortOrder;

    @Builder
    public CreateStatusRequest(Long projectId, String name, String colorCode, Short sortOrder) {
        this.projectId = projectId;
        this.name = name;
        this.colorCode = colorCode;
        this.sortOrder = sortOrder;
    }

    public CreateStatusDto toServiceDto() {
        return null;
    }
}
