package com.growup.pms.test.fixture.auth.builder;

import com.growup.pms.auth.service.dto.oauth.kakao.KakaoAccount;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoProfile;
import com.growup.pms.auth.service.dto.oauth.kakao.Profile;
import com.growup.pms.auth.service.dto.oauth.kakao.Properties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoProfileTestBuilder {

    private Long id = 12345L;
    private String connectedAt = "2023-01-01T00:00:00Z";
    private String profileImage = "http://profile-image.com";
    private String thumbnailImage = "http://thumbnail-image.com";
    private Boolean isEmailVerified = true;
    private Boolean hasEmail = true;
    private String profileImageUrl = "http://profile-image.com";
    private String nickname = "tester";
    private String email = "test@test.com";

    public static KakaoProfileTestBuilder 카카오_프로필은() {
        return new KakaoProfileTestBuilder();
    }

    public KakaoProfileTestBuilder 아이디가(Long 아이디) {
        this.id = 아이디;
        return this;
    }

    public KakaoProfileTestBuilder 닉네임이(String 닉네임) {
        this.nickname = 닉네임;
        return this;
    }

    public KakaoProfileTestBuilder 이메일이(String 이메일) {
        this.email = 이메일;
        return this;
    }

    public KakaoProfile 이다() {
        return KakaoProfile.builder()
                .id(id)
                .connectedAt(connectedAt)
                .properties(Properties.builder()
                        .nickname(nickname)
                        .profileImage(profileImage)
                        .thumbnailImage(thumbnailImage)
                        .build())
                .kakaoAccount(KakaoAccount.builder()
                        .email(email)
                        .isEmailVerified(isEmailVerified)
                        .hasEmail(hasEmail)
                        .profile(Profile.builder()
                                .nickname(nickname)
                                .profileImageUrl(profileImageUrl)
                                .build())
                        .build()
                )
                .build();
    }
}
