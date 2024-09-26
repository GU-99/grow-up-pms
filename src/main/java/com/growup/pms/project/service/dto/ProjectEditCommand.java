package com.growup.pms.project.service.dto;

import java.time.LocalDate;
import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record ProjectEditCommand(JsonNullable<String> projectName, JsonNullable<String> content,
                                 JsonNullable<LocalDate> startDate, JsonNullable<LocalDate> endDate) {

}
