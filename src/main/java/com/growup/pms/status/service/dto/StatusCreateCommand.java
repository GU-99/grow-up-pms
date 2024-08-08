package com.growup.pms.status.service.dto;

import lombok.Builder;

@Builder
public record StatusCreateCommand(Long projectId, String name, String colorCode, Short sortOrder) {
}
