package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.controller.dto.request.NicknameDuplicateCheckRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NicknameDuplicateCheckRequestTestBuilder {
    private String nickname = "브라운";

    public static NicknameDuplicateCheckRequestTestBuilder 닉네임_중복_검사는() {
        return new NicknameDuplicateCheckRequestTestBuilder();
    }

    public NicknameDuplicateCheckRequestTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public NicknameDuplicateCheckRequest 이다() {
        return NicknameDuplicateCheckRequest.builder()
                .nickname(nickname)
                .build();
    }
}
