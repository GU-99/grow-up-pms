package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserLink;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLinkTestBuilder {
    private Long id = 1L;
    private User user;
    private String link = "https://github.com/growup";

    public static UserLinkTestBuilder 링크는() {
        return new UserLinkTestBuilder();
    }

    public UserLinkTestBuilder 식별자가(Long 식별자) {
        this.id = 식별자;
        return this;
    }

    public UserLinkTestBuilder 사용자가(User 사용자) {
        this.user = 사용자;
        return this;
    }

    public UserLinkTestBuilder 링크가(String 링크) {
        this.link = 링크;
        return this;
    }

    public UserLink 이다() {
        var userLink = UserLink.builder()
                .user(user)
                .link(link)
                .build();
        ReflectionTestUtils.setField(userLink, "id", id);
        return userLink;
    }
}
