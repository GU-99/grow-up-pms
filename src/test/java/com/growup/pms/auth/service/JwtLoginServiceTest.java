package com.growup.pms.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.auth.dto.LoginRequest;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenDto;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.auth.LoginRequestFixture;
import com.growup.pms.test.fixture.auth.TokenDtoFixture;
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
    RefreshTokenService refreshTokenService;

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
            Long userId = 1L;
            Long newRefreshTokenId = 1L;
            LoginRequest validRequest = LoginRequestFixture.createDefaultRequest();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(validRequest.getEmail(), validRequest.getPassword());
            TokenDto expectedNewToken = TokenDtoFixture.createDefaultDto();

            when(authenticationManager.authenticate(token)).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(securityUser);
            when(tokenProvider.generateToken(securityUser)).thenReturn(expectedNewToken);
            when(securityUser.getId()).thenReturn(userId);
            when(refreshTokenService.renewRefreshToken(userId, expectedNewToken.getRefreshToken())).thenReturn(newRefreshTokenId);

            // when
            TokenDto actualNewToken = loginService.authenticateUser(validRequest);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actualNewToken).isNotNull();
                softly.assertThat(expectedNewToken).isEqualTo(actualNewToken);
            });
        }

        @Test
        void 실패하면_예외가_발생한다() {
            // given
            LoginRequest badRequest = LoginRequestFixture.createDefaultRequest();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(badRequest.getEmail(), badRequest.getPassword());

            doThrow(new RuntimeException()).when(authenticationManager).authenticate(token);

            // when & then
            assertThatThrownBy(() -> loginService.authenticateUser(badRequest))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
