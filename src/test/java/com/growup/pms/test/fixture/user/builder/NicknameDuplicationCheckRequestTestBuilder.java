package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.controller.dto.request.NicknameDuplicationCheckRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NicknameDuplicationCheckRequestTestBuilder {
    private String nickname = "브라운";

    public static NicknameDuplicationCheckRequestTestBuilder 닉네임_중복_검사는() {
        return new NicknameDuplicationCheckRequestTestBuilder();
    }

    public NicknameDuplicationCheckRequestTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public NicknameDuplicationCheckRequest 이다() {
        return NicknameDuplicationCheckRequest.builder()
                .nickname(nickname)
                .build();
    }
}
