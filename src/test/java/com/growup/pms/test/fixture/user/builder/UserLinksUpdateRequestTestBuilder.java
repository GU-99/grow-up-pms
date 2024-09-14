package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.controller.dto.request.UserLinksUpdateRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLinksUpdateRequestTestBuilder {

    private List<String> links = List.of("http://github.com", "http://blog.example.com", "http://GU-99.com");

    public static UserLinksUpdateRequestTestBuilder 사용자_링크_변경_요청은() {
        return new UserLinksUpdateRequestTestBuilder();
    }

    public UserLinksUpdateRequestTestBuilder 링크가(List<String> 링크) {
        this.links = 링크;
        return this;
    }

    public UserLinksUpdateRequest 이다() {
        return UserLinksUpdateRequest.builder()
                .links(JsonNullable.of(links))
                .build();
    }
}
