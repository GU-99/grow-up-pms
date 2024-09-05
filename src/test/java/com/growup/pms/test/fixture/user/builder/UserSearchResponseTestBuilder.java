package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSearchResponseTestBuilder {
    private Long userId = 1L;
    private String nickname = "브라운";

    public static UserSearchResponseTestBuilder 사용자_검색_응답은() {
        return new UserSearchResponseTestBuilder();
    }

    public UserSearchResponseTestBuilder 식별자가(Long 식별자가) {
        this.userId = 식별자가;
        return this;
    }

    public UserSearchResponseTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public UserSearchResponse 이다() {
        return UserSearchResponse.builder()
                .userId(userId)
                .nickname(nickname)
                .build();
    }
}
