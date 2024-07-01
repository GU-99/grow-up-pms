package com.growup.pms.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.dto.TokenDto;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.auth.TokenDtoFixture;
import com.growup.pms.test.fixture.user.UserFixture;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {
    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private JwtTokenService tokenService;

    @Test
    void 토큰을_새롭게_생성한다() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                UserFixture.DEFAULT_EMAIL, UserFixture.DEFAULT_PASSWORD, Collections.emptyList());
        TokenDto expectedTokenDto = TokenDtoFixture.createDefaultDtoBuilder()
                        .accessToken(TokenDtoFixture.NEW_ACCESS_TOKEN)
                        .refreshToken(TokenDtoFixture.NEW_REFRESH_TOKEN)
                        .build();

        when(tokenProvider.createAccessToken(anyLong(), any(Authentication.class))).thenReturn(TokenDtoFixture.NEW_ACCESS_TOKEN);
        when(tokenProvider.createRefreshToken(anyLong(), any(Authentication.class))).thenReturn(TokenDtoFixture.NEW_REFRESH_TOKEN);

        // when
        TokenDto actualTokenDto = tokenService.generateJwtTokens(0L, authentication);

        // then
        assertThat(actualTokenDto.getAccessToken()).isEqualTo(expectedTokenDto.getAccessToken());
        assertThat(actualTokenDto.getRefreshToken()).isEqualTo(expectedTokenDto.getRefreshToken());
    }
}
