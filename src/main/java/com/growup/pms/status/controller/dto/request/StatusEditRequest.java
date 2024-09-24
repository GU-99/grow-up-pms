package com.growup.pms.status.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.COLOR_CODE_PATTERN;

import com.growup.pms.status.service.dto.StatusEditCommand;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusEditRequest {

    @Size(min = 1, max = 32)
    private JsonNullable<String> statusName = JsonNullable.undefined();

    @Size(min = 7, max = 7)
    @Pattern(regexp = COLOR_CODE_PATTERN)
    private JsonNullable<String> colorCode = JsonNullable.undefined();

    @Builder
    public StatusEditRequest(JsonNullable<String> statusName, JsonNullable<String> colorCode) {
        this.statusName = statusName;
        this.colorCode = colorCode;
    }

    public StatusEditCommand toCommand(Long statusId) {
        return StatusEditCommand.builder()
                .statusId(statusId)
                .statusName(statusName)
                .colorCode(colorCode)
                .build();
    }
}
