package com.growup.pms.status.controller.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusResponse {

    private Long statusId;
    private Long projectId;
    private String name;
    private String colorCode;
    private Short sortOrder;

    @Builder
    public StatusResponse(Long statusId, Long projectId, String name, String colorCode, Short sortOrder) {
        this.statusId = statusId;
        this.projectId = projectId;
        this.name = name;
        this.colorCode = colorCode;
        this.sortOrder = sortOrder;
    }
}
