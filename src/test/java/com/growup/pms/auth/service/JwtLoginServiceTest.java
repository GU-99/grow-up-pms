package com.growup.pms.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.auth.dto.SignInRequest;
import com.growup.pms.auth.dto.TokenDto;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.auth.SignInRequestFixture;
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
    JwtTokenService tokenService;

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
            SignInRequest validRequest = SignInRequestFixture.createDefaultRequest();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(validRequest.getUsername(), validRequest.getPassword());
            TokenDto expectedToken = TokenDtoFixture.createDefaultDto();

            when(authenticationManager.authenticate(token)).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(securityUser);
            when(securityUser.getId()).thenReturn(userId);
            when(tokenService.generateJwtTokens(userId, authentication)).thenReturn(expectedToken);

            // when
            TokenDto actualToken = loginService.authenticateUser(validRequest);

            // then
            assertThat(actualToken).isNotNull();
            assertThat(expectedToken.getAccessToken()).isEqualTo(actualToken.getAccessToken());
            assertThat(expectedToken.getRefreshToken()).isEqualTo(actualToken.getRefreshToken());

            verify(authenticationManager).authenticate(token);
            verify(tokenService).generateJwtTokens(userId, authentication);
        }

        @Test
        void 실패하면_예외가_발생한다() {
            // given
            SignInRequest badRequest = SignInRequestFixture.createDefaultRequest();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(badRequest.getUsername(), badRequest.getPassword());

            doThrow(new RuntimeException()).when(authenticationManager).authenticate(token);

            // when & then
            assertThatThrownBy(() -> loginService.authenticateUser(badRequest))
                    .isInstanceOf(RuntimeException.class);

            verify(authenticationManager).authenticate(token);
        }
    }
}
