package com.growup.pms.auth.service;

import static com.growup.pms.test.fixture.auth.SecurityUserTestBuilder.인증된_사용자는;
import static com.growup.pms.test.fixture.auth.TokenResponseTestBuilder.발급된_토큰은;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {
    @Mock
    JwtTokenProvider tokenProvider;

    @Mock
    RefreshTokenService refreshTokenService;

    @InjectMocks
    JwtTokenService tokenService;

    @Nested
    class 토큰을_갱신_시에 {
        @Test
        void 성공한다() {
            // given
            Long 기존_사용자_ID = 1L;
            Long 새_리프레시_토큰_ID = 1L;
            Authentication 인증_정보 = mock(Authentication.class);
            SecurityUser 인증된_사용자 = 인증된_사용자는().식별자가(기존_사용자_ID).이다();
            String 예전_리프레시_토큰 = "예전 리프레시 토큰";
            TokenResponse 예상하는_새_토큰 = 발급된_토큰은().액세스_토큰이("새 액세스 토큰").리프레시_토큰이("새 리프레시 토큰").이다();

            when(인증_정보.getPrincipal()).thenReturn(인증된_사용자);
            when(tokenProvider.getAuthentication(예전_리프레시_토큰)).thenReturn(인증_정보);
            when(tokenProvider.generateToken(인증된_사용자)).thenReturn(예상하는_새_토큰);
            when(refreshTokenService.validateToken(예전_리프레시_토큰)).thenReturn(true);
            when(refreshTokenService.renewRefreshToken(기존_사용자_ID, 예상하는_새_토큰.refreshToken())).thenReturn(새_리프레시_토큰_ID);

            // when
            TokenResponse 실제_토큰 = tokenService.refreshJwtTokens(예전_리프레시_토큰);

            // then
            assertThat(실제_토큰).isEqualTo(예상하는_새_토큰);
        }

        @Test
        void 토큰이_유효하지_않은_경우_예외가_발생한다() {
            // given
            String 유효하지_않은_리프레시_토큰 = "유효하지 않은 리프레시 토큰";

            doThrow(AuthenticationException.class).when(refreshTokenService).validateToken(유효하지_않은_리프레시_토큰);

            // when & then
            assertThatThrownBy(() -> tokenService.refreshJwtTokens(유효하지_않은_리프레시_토큰))
                    .isInstanceOf(AuthenticationException.class);
        }
    }
}
