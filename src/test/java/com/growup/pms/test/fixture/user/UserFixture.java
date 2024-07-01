package com.growup.pms.test.fixture.user;

import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {
    public static final String DEFAULT_EMAIL = "ryan@example.com";
    public static final String DEFAULT_PASSWORD = "!test1234";
    public static final String DEFAULT_NICKNAME = "라이언";
    public static final String DEFAULT_BIO = "안녕하세요, 라이언입니다!";
    public static final String DEFAULT_IMAGE = "https://example.com/profile/ryan.jpg";

    public static final String INVALID_EMAIL = "ryan";
    public static final String INVALID_PASSWORD = "!test";
    public static final String INVALID_NICKNAME = " ";

    public static User createUserWithId(Long id) {
        User user = createDefaultUserBuilder().build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public static User.UserBuilder createDefaultUserBuilder() {
        return User.builder()
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .provider(Provider.LOCAL)
                .profile(UserProfile.builder()
                        .nickname(DEFAULT_NICKNAME)
                        .bio(DEFAULT_BIO)
                        .image(DEFAULT_IMAGE)
                        .build());
    }
}
