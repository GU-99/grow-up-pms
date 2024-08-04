package com.growup.pms.team.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
// TODO: 역할이나 권한이 구현되면 API 명세에 있는 coworker 필드를 추가해야 함
public class TeamCreateRequest {
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$", message = "팀 이름은 한글, 영문, 숫자로 구성된 1~10자리여야 합니다.")
    private String name;

    private String content;

    @Builder
    public TeamCreateRequest(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public static TeamCreateDto toServiceDto(TeamCreateRequest request) {
        return TeamCreateDto.builder()
                .name(request.getName())
                .content(request.getContent())
                .build();
    }
}
