package com.growup.pms.team.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamUpdateRequest {
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$", message = "팀 이름은 한글, 영문, 숫자로 구성된 1~10자리여야 합니다.")
    private JsonNullable<String> name = JsonNullable.undefined();

    private JsonNullable<String> content = JsonNullable.undefined();

    @Builder
    public TeamUpdateRequest(JsonNullable<String> name, JsonNullable<String> content) {
        this.name = name;
        this.content = content;
    }

    public static TeamUpdateDto toServiceDto(TeamUpdateRequest request) {
        return TeamUpdateDto.builder()
                .name(request.getName())
                .content(request.getContent())
                .build();
    }
}
