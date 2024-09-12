package com.growup.pms.test.fixture.user.builder;

import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTestBuilder {
    private Long id = 1L;
    private String username = "brown";
    private String password = "test1234!@#$";
    private String email = "brown@example.com";
    private String nickname = "브라운";
    private Provider provider = Provider.LOCAL;
    private String bio = "안녕하세요, 브라운입니다!";
    private String profileImageName = "728f3af8-4080-45b1-8b3c-d25e5e073dc7.png";

    public static UserTestBuilder 사용자는() {
        return new UserTestBuilder();
    }

    public UserTestBuilder 식별자가(Long 식별자) {
        this.id = 식별자;
        return this;
    }

    public UserTestBuilder 아이디가(String 아이디) {
        this.username = 아이디;
        return this;
    }

    public UserTestBuilder 비밀번호가(String 비밀번호) {
        this.password = 비밀번호;
        return this;
    }

    public UserTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public UserTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public UserTestBuilder 인증_프로바이더가(Provider 인증_프로바이더) {
        this.provider = 인증_프로바이더;
        return this;
    }

    public UserTestBuilder 자기소개가(String 자기소개) {
        this.bio = 자기소개;
        return this;
    }

    public UserTestBuilder 프로필_이미지_이름이(String 프로필_이미지_이름) {
        this.profileImageName = 프로필_이미지_이름;
        return this;
    }

    public User 이다() {
        var user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .provider(provider)
                .profile(UserProfile.builder()
                        .nickname(nickname)
                        .bio(bio)
                        .imageName(profileImageName)
                        .build())
                .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
