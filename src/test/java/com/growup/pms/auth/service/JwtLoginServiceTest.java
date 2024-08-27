package com.growup.pms.auth.service;

import static com.growup.pms.test.fixture.auth.LoginRequestTestBuilder.로그인_하는_사용자는;
import static com.growup.pms.test.fixture.auth.TokenResponseTestBuilder.발급된_토큰은;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.service.dto.LoginCommand;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class JwtLoginServiceTest {
    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @Mock
    SecurityUser securityUser;

    @Mock
    RedisRefreshTokenService refreshTokenService;

    @Mock
    JwtTokenProvider tokenProvider;

    @InjectMocks
    JwtLoginService loginService;

    @BeforeEach
    void setUp() {
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
    }

    @Nested
    class 사용자가_인증에 {
        @Test
        void 성공한다() {
            // given
            Long 기존_사용자_ID = 1L;
            LoginCommand 유효한_로그인_요청 = 로그인_하는_사용자는().이다().toCommand();
            UsernamePasswordAuthenticationToken 인증_토큰 = new UsernamePasswordAuthenticationToken(유효한_로그인_요청.username(), 유효한_로그인_요청.password());
            TokenResponse 예상하는_새_토큰 = 발급된_토큰은().이다();

            when(authenticationManager.authenticate(인증_토큰)).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(securityUser);
            when(tokenProvider.generateToken(securityUser)).thenReturn(예상하는_새_토큰);
            when(securityUser.getId()).thenReturn(기존_사용자_ID);

            // when
            TokenResponse 실제_새_토큰 = loginService.authenticateUser(유효한_로그인_요청);

            // then
            assertSoftly(softly -> {
                softly.assertThat(실제_새_토큰).isNotNull();
                softly.assertThat(실제_새_토큰).isEqualTo(예상하는_새_토큰);
            });
        }

        @Test
        void 실패하면_예외가_발생한다() {
            // given
            LoginCommand 잘못된_요청 = 로그인_하는_사용자는().아이디가("존재하지 않는 아이디").이다().toCommand();
            UsernamePasswordAuthenticationToken 인증_토큰 = new UsernamePasswordAuthenticationToken(잘못된_요청.username(), 잘못된_요청.password());

            doThrow(new RuntimeException()).when(authenticationManager).authenticate(인증_토큰);

            // when & then
            assertThatThrownBy(() -> loginService.authenticateUser(잘못된_요청))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
