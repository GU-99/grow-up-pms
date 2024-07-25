package com.growup.pms.status.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateStatusDto {

    private Long projectId;
    private Long name;
    private Long colorCode;
    private Long sortOrder;

    @Builder
    public CreateStatusDto(Long projectId, Long name, Long colorCode, Long sortOrder) {
        this.projectId = projectId;
        this.name = name;
        this.colorCode = colorCode;
        this.sortOrder = sortOrder;
    }
}
