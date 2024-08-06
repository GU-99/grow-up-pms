package com.growup.pms.status.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusEditDto {

    private Long statusId;
    private String statusName;
    private String colorCode;

    @Builder
    public StatusEditDto(Long statusId, String statusName, String colorCode) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.colorCode = colorCode;
    }
}
