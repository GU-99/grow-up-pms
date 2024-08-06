package com.growup.pms.status.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusEditDto {

    private Long statusId;
    private JsonNullable<String> statusName;
    private JsonNullable<String> colorCode;
    private JsonNullable<Short> sortOrder;

    @Builder
    public StatusEditDto(Long statusId, JsonNullable<String> statusName, JsonNullable<String> colorCode,
                         JsonNullable<Short> sortOrder) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.colorCode = colorCode;
        this.sortOrder = sortOrder;
    }
}
