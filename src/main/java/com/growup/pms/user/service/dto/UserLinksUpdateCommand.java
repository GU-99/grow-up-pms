package com.growup.pms.user.service.dto;

import java.util.List;
import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record UserLinksUpdateCommand(JsonNullable<List<String>> links) {
}
