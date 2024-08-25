package com.growup.pms.team.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.TEAM_NAME_PATTERN;

import com.growup.pms.team.service.dto.TeamUpdateCommand;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamUpdateRequest {
    @Pattern(regexp = TEAM_NAME_PATTERN)
    private JsonNullable<String> name = JsonNullable.undefined();

    @Length(max = 300)
    private JsonNullable<String> content = JsonNullable.undefined();

    @Builder
    public TeamUpdateRequest(JsonNullable<String> name, JsonNullable<String> content) {
        this.name = name;
        this.content = content;
    }

    public TeamUpdateCommand toCommand() {
        return TeamUpdateCommand.builder()
                .name(name)
                .content(content)
                .build();
    }
}
