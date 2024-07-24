package com.growup.pms.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenDto;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.auth.TokenDtoFixture;
import com.growup.pms.test.fixture.common.SecurityUserFixture;
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
            Long userId = 1L;
            Long newRefreshTokenId = 1L;
            Authentication authentication = mock(Authentication.class);
            SecurityUser user = SecurityUserFixture.createUserWithUserId(userId);
            String oldRefreshToken = "oldRefreshToken";
            TokenDto expectedNewToken = TokenDtoFixture.createDefaultDtoBuilder()
                    .accessToken(TokenDtoFixture.NEW_ACCESS_TOKEN)
                    .refreshToken(TokenDtoFixture.NEW_REFRESH_TOKEN)
                    .build();

            when(authentication.getPrincipal()).thenReturn(user);
            when(tokenProvider.getAuthentication(oldRefreshToken)).thenReturn(authentication);
            when(tokenProvider.generateToken(user)).thenReturn(expectedNewToken);
            when(refreshTokenService.validateToken(oldRefreshToken)).thenReturn(true);
            when(refreshTokenService.renewRefreshToken(userId, expectedNewToken.getRefreshToken())).thenReturn(newRefreshTokenId);

            // when
            TokenDto actualNewToken = tokenService.refreshJwtTokens(oldRefreshToken);

            // then
            assertThat(actualNewToken).isEqualTo(expectedNewToken);
        }

        @Test
        void 토큰이_유효하지_않은_경우_예외가_발생한다() {
            // given
            String invalidRefreshToken = "invalidRefreshToken";

            doThrow(AuthenticationException.class).when(refreshTokenService).validateToken(invalidRefreshToken);

            // when & then
            assertThatThrownBy(() -> tokenService.refreshJwtTokens(invalidRefreshToken))
                    .isInstanceOf(AuthenticationException.class);
        }
    }
}
