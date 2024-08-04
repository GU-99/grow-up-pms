package com.growup.pms.team.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamUpdateDto {
    private JsonNullable<String> name = JsonNullable.undefined();
    private JsonNullable<String> content = JsonNullable.undefined();

    @Builder
    public TeamUpdateDto(JsonNullable<String> name, JsonNullable<String> content) {
        this.name = name;
        this.content = content;
    }
}
