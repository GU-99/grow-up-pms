package com.growup.pms.auth.service;

import static com.growup.pms.test.fixture.auth.builder.GoogleAccessTokenTestBuilder.구글_액세스_토큰은;
import static com.growup.pms.test.fixture.auth.builder.GoogleProfileTestBuilder.구글_프로필은;
import static com.growup.pms.test.fixture.auth.builder.KakaoAccessTokenTestBuilder.카카오_액세스_토큰은;
import static com.growup.pms.test.fixture.auth.builder.KakaoProfileTestBuilder.카카오_프로필은;
import static com.growup.pms.test.fixture.auth.builder.TokenResponseTestBuilder.발급된_토큰은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static com.growup.pms.user.domain.Provider.GOOGLE;
import static com.growup.pms.user.domain.Provider.KAKAO;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.service.dto.oauth.OauthAccessToken;
import com.growup.pms.auth.service.dto.oauth.OauthProfile;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoProfile;
import com.growup.pms.auth.service.oauth.GoogleOauth2Service;
import com.growup.pms.auth.service.oauth.KakaoOauth2Service;
import com.growup.pms.auth.service.oauth.OauthLoginService;
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
class OauthLoginServiceTest {

    @Mock
    private KakaoOauth2Service kakaoOauth2Service;

    @Mock
    private GoogleOauth2Service googleOauth2Service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenService redisRefreshTokenService;

    @InjectMocks
    private OauthLoginService oAuthLoginService;

    @Nested
    class 소셜로그인을_통한_인증에 {

        @Test
        void 카카오_로그인으로_성공한다() {

            // Given
            OauthAccessToken 카카오_액세스_토큰 = 카카오_액세스_토큰은().이다();
            KakaoProfile 카카오_프로필 = 카카오_프로필은().이메일이("test@gmail.com").이다();
            User 유저 = 사용자는().이메일이("test@gmail.com").아이디가("test@gmail.com").인증_프로바이더가(KAKAO).이다();
            TokenResponse 발급될_토큰 = 발급된_토큰은().이다();
            Provider 공급자 = KAKAO;
            String 인가코드 = "test_code";

            when(kakaoOauth2Service.requestToken(공급자, 인가코드)).thenReturn(카카오_액세스_토큰);
            when(kakaoOauth2Service.requestProfile(공급자, 카카오_액세스_토큰)).thenReturn(카카오_프로필);

            when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(유저));
            when(jwtTokenProvider.generateToken(any(SecurityUser.class))).thenReturn(발급될_토큰);

            // When
            TokenResponse tokenResponse = oAuthLoginService.authenticate(공급자, 인가코드);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(tokenResponse).isNotNull();
                softly.assertThat(tokenResponse.accessToken()).isEqualTo("액세스 토큰");
                softly.assertThat(tokenResponse.refreshToken()).isEqualTo("리프레시 토큰");
            });

            verify(kakaoOauth2Service, times(1)).requestToken(공급자, 인가코드);
            verify(kakaoOauth2Service, times(1)).requestProfile(공급자, 카카오_액세스_토큰);
            verify(userRepository, times(1)).findByEmail("test@gmail.com");
            verify(jwtTokenProvider, times(1)).generateToken(any(SecurityUser.class));
        }

        @Test
        void 구글_로그인으로_성공한다() {

            // Given
            OauthAccessToken 구글_액세스_토큰 = 구글_액세스_토큰은().이다();
            OauthProfile 구글_프로필 = 구글_프로필은().이메일이("test@gmail.com").이다();
            TokenResponse 발급될_토큰 = 발급된_토큰은().이다();
            User 유저 = 사용자는().이메일이("test@gmail.com").아이디가("test@gmail.com").인증_프로바이더가(GOOGLE).이다();
            Provider 공급자 = GOOGLE;
            String 인가코드 = "test_code";

            when(googleOauth2Service.requestToken(공급자, 인가코드)).thenReturn(구글_액세스_토큰);
            when(googleOauth2Service.requestProfile(공급자, 구글_액세스_토큰)).thenReturn(구글_프로필);

            when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(유저));
            when(jwtTokenProvider.generateToken(any(SecurityUser.class))).thenReturn(발급될_토큰);

            // When
            TokenResponse tokenResponse = oAuthLoginService.authenticate(공급자, 인가코드);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(tokenResponse).isNotNull();
                softly.assertThat(tokenResponse.accessToken()).isEqualTo("액세스 토큰");
                softly.assertThat(tokenResponse.refreshToken()).isEqualTo("리프레시 토큰");
            });

            verify(googleOauth2Service, times(1)).requestToken(공급자, 인가코드);
            verify(googleOauth2Service, times(1)).requestProfile(공급자, 구글_액세스_토큰);
            verify(userRepository, times(1)).findByEmail("test@gmail.com");
            verify(jwtTokenProvider, times(1)).generateToken(any(SecurityUser.class));
        }
    }
}
