package com.growup.pms.status.service.dto;

import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record StatusEditCommand(
        Long statusId,
        JsonNullable<String> statusName,
        JsonNullable<String> colorCode
) {
}
