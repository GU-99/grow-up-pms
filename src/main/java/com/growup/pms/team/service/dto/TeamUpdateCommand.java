package com.growup.pms.team.service.dto;

import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record TeamUpdateCommand(JsonNullable<String> teamName, JsonNullable<String> content) {
}
