package com.growup.pms.team.controller.dto.request;

import com.growup.pms.team.service.dto.TeamCreateCommand;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record TeamCreateRequest(
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$", message = "팀 이름은 한글, 영문, 숫자로 구성된 1~10자리여야 합니다.")
        String name,

        String content
) {
    public TeamCreateCommand toCommand() {
        return TeamCreateCommand.builder()
                .name(name())
                .content(content)
                .build();
    }
}
