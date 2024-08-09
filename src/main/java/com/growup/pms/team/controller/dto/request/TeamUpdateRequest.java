package com.growup.pms.team.controller.dto.request;

import com.growup.pms.team.service.dto.TeamUpdateCommand;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamUpdateRequest {
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$")
    private JsonNullable<String> name = JsonNullable.undefined();

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
