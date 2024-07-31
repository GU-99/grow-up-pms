package com.growup.pms.status.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusCreateDto {

    private Long projectId;
    private String name;
    private String colorCode;
    private Short sortOrder;

    @Builder
    public StatusCreateDto(Long projectId, String name, String colorCode, Short sortOrder) {
        this.projectId = projectId;
        this.name = name;
        this.colorCode = colorCode;
        this.sortOrder = sortOrder;
    }
}
