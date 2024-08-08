package com.growup.pms.status.controller.dto.response;

import lombok.Builder;

@Builder
public record StatusResponse(Long statusId, Long projectId, String name, String colorCode, Short sortOrder) {
}
