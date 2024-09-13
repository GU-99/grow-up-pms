package com.growup.pms.auth.service;

import static com.growup.pms.test.fixture.auth.builder.GoogleAccessTokenTestBuilder.구글_액세스_토큰은;
import static com.growup.pms.test.fixture.auth.builder.GoogleProfileTestBuilder.구글_프로필은;
import static com.growup.pms.test.fixture.auth.builder.KakaoAccessTokenTestBuilder.카카오_액세스_토큰은;
import static com.growup.pms.test.fixture.auth.builder.KakaoProfileTestBuilder.카카오_프로필은;
import static com.growup.pms.test.fixture.auth.builder.TokenResponseTestBuilder.발급된_토큰은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.service.dto.oauth.google.GoogleAccessToken;
import com.growup.pms.auth.service.dto.oauth.google.GoogleProfile;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoAccessToken;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoProfile;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OAuthLoginServiceTest {
    @Mock
    private KakaoOAuth2Service kakaoOAuth2Service;

    @Mock
    private GoogleOAuth2Service googleOAuth2Service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenService redisRefreshTokenService;

    @InjectMocks
    private OAuthLoginService oAuthLoginService;

    @Nested
    class 소셜로그인을_통한_인증에 {

        @Test
        void 카카오_로그인으로_성공한다() {
            // given
            String 인가코드 = "mock-code";
            KakaoAccessToken 카카오_액세스_토큰 = 카카오_액세스_토큰은().이다();
            KakaoProfile 카카오_프로필 = 카카오_프로필은().이다();
            TokenResponse 예상하는_새_토큰 = 발급된_토큰은().이다();
            User 유저 = 사용자는().닉네임이("tester").이메일이("test@test.com").인증_프로바이더가(Provider.KAKAO).이다();

            when(kakaoOAuth2Service.requestToken(anyString())).thenReturn(카카오_액세스_토큰);
            when(kakaoOAuth2Service.requestProfile(카카오_액세스_토큰)).thenReturn(카카오_프로필);
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(유저));
            when(jwtTokenProvider.generateToken(any())).thenReturn(예상하는_새_토큰);

            // when
            TokenResponse response = oAuthLoginService.authenticateOfKakao(인가코드);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response).isNotNull();
                softly.assertThat(response)
                        .extracting("accessToken", "refreshToken")
                        .contains("액세스 토큰", "리프레시 토큰");
            });

            verify(kakaoOAuth2Service, times(1)).requestToken(anyString());
            verify(kakaoOAuth2Service, times(1)).requestProfile(카카오_액세스_토큰);
            verify(userRepository, times(1)).findByEmail(anyString());
            verify(jwtTokenProvider, times(1)).generateToken(any());
        }

        @Test
        void 구글_로그인으로_성공한다() {
            // given
            String 인가코드 = "mock-code";
            User 유저 = 사용자는().닉네임이("tester").이메일이("test@test.com").인증_프로바이더가(Provider.GOOGLE).이다();
            TokenResponse 예상하는_새_토큰 = 발급된_토큰은().이다();
            GoogleAccessToken 구글_액세스_토큰 = 구글_액세스_토큰은().이다();
            GoogleProfile 구글_프로필 = 구글_프로필은().이다();

            when(googleOAuth2Service.requestToken(anyString())).thenReturn(구글_액세스_토큰);
            when(googleOAuth2Service.requestProfile(구글_액세스_토큰)).thenReturn(구글_프로필);
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(유저));
            when(jwtTokenProvider.generateToken(any())).thenReturn(예상하는_새_토큰);

            // when
            TokenResponse response = oAuthLoginService.authenticateOfGoogle(인가코드);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response).isNotNull();
                softly.assertThat(response)
                        .extracting("accessToken", "refreshToken")
                        .contains("액세스 토큰", "리프레시 토큰");
            });

            verify(googleOAuth2Service, times(1)).requestToken(anyString());
            verify(googleOAuth2Service, times(1)).requestProfile(구글_액세스_토큰);
            verify(userRepository, times(1)).findByEmail(anyString());
            verify(jwtTokenProvider, times(1)).generateToken(any());
        }
    }
}
