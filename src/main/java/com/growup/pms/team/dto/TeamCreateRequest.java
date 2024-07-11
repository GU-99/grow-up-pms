package com.growup.pms.team.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growup.pms.team.domain.Team;
import com.growup.pms.user.domain.User;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonProperty("creator")
    private Long creatorId;

    @Builder
    public TeamCreateRequest(String name, String content, Long creatorId) {
        this.name = name;
        this.content = content;
        this.creatorId = creatorId;
    }

    public static Team toEntity(TeamCreateRequest request, User creator) {
        return Team.builder()
                .name(request.getName())
                .content(request.getContent())
                .creator(creator)
                .build();
    }
}
