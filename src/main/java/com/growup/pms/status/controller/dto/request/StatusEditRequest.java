package com.growup.pms.status.controller.dto.request;

import com.growup.pms.status.service.dto.StatusEditDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusEditRequest {

    @NotBlank
    @Size(min = 1, max = 32)
    private String statusName;

    @NotBlank
    @Size(min = 1, max = 6)
    private String colorCode;

    @Builder
    public StatusEditRequest(String statusName, String colorCode) {
        this.statusName = statusName;
        this.colorCode = colorCode;
    }

    public StatusEditDto toServiceDto(Long statusId) {
        return StatusEditDto.builder()
                .statusId(statusId)
                .statusName(statusName)
                .colorCode(colorCode)
                .build();
    }
}
