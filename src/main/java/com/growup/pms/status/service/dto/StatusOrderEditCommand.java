package com.growup.pms.status.service.dto;

import lombok.Builder;

@Builder
public record StatusOrderEditCommand(
        Long statusId,
        Short sortOrder
) {

}
