package com.growup.pms.test.fixture.project.builder;

import com.growup.pms.project.controller.dto.response.ProjectUserSearchResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectUserSearchResponseTestBuilder {

    private Long userId = 1L;
    private String nickname = "브라운";

    public static ProjectUserSearchResponseTestBuilder 검색된_프로젝트원은() {
        return new ProjectUserSearchResponseTestBuilder();
    }

    public ProjectUserSearchResponseTestBuilder 회원_식별자가(Long userId) {
        this.userId = userId;
        return this;
    }

    public ProjectUserSearchResponseTestBuilder 닉네임이(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public ProjectUserSearchResponse 이다() {
        return ProjectUserSearchResponse.builder()
                .userId(userId)
                .nickname(nickname)
                .build();
    }
}
