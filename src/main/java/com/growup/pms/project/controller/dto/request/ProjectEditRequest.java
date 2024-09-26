package com.growup.pms.project.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.LOCAL_DATE_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.growup.pms.project.service.dto.ProjectEditCommand;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectEditRequest {

    @Size(max = 128)
    private JsonNullable<String> projectName = JsonNullable.undefined();

    @Size(max = 200)
    private JsonNullable<String> content = JsonNullable.undefined();

    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    private JsonNullable<LocalDate> startDate = JsonNullable.undefined();

    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    private JsonNullable<LocalDate> endDate = JsonNullable.undefined();

    @Builder
    public ProjectEditRequest(JsonNullable<String> projectName, JsonNullable<String> content,
                              JsonNullable<LocalDate> startDate, JsonNullable<LocalDate> endDate) {
        this.projectName = projectName;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ProjectEditCommand toCommand() {
        return ProjectEditCommand.builder()
                .projectName(projectName)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
