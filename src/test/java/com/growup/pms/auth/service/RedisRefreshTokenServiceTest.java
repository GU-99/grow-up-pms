package com.growup.pms.auth.service;

import static com.growup.pms.test.fixture.auth.SecurityUserTestBuilder.인증된_사용자는;
import static com.growup.pms.test.fixture.auth.TokenResponseTestBuilder.발급된_토큰은;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class RedisRefreshTokenServiceTest {
    @Mock
    JwtTokenProvider tokenProvider;

    @Mock
    StringRedisTemplate stringRedisTemplate;

    @Mock
    ValueOperations<String, String> valueOperations;

    @InjectMocks
    RedisRefreshTokenService refreshTokenService;

    @Nested
    class 리프레시_토큰을_저장_시에 {
        @BeforeEach
        void setUp() {
            when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        }

        @Test
        void 성공한다() {
            // given
            Long 사용자_ID = 1L;
            String 유효한_리프레시_토큰 = "유효한 리프레시 토큰";

            doNothing().when(valueOperations).set(anyString(), eq(""), any(Duration.class));

            // when & then
            assertThatCode(() -> refreshTokenService.save(사용자_ID, 유효한_리프레시_토큰))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class 리프레시_토큰을_제거_시에 {
        @Test
        void 성공한다() {
            // given
            Long 사용자_ID = 1L;
            String 유효한_리프레시_토큰 = "유효한 리프레시 토큰";

            when(stringRedisTemplate.delete(anyString())).thenReturn(true);

            // when & then
            assertThatCode(() -> refreshTokenService.revoke(사용자_ID, 유효한_리프레시_토큰))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class 토큰을_갱신_시에 {
        @Test
        void 성공한다() {
            // given
            Long 기존_사용자_ID = 1L;
            String 기존_리프레시_토큰 = "기존 리프레시 토큰";
            SecurityUser 인증된_사용자 = 인증된_사용자는().식별자가(기존_사용자_ID).이다();
            Authentication 인증_정보 = mock(Authentication.class);
            TokenResponse 예상하는_새_토큰 = 발급된_토큰은().액세스_토큰이("새 액세스 토큰").리프레시_토큰이("새 리프레시 토큰").이다();

            when(인증_정보.getPrincipal()).thenReturn(인증된_사용자);
            when(tokenProvider.validateToken(anyString())).thenReturn(true);
            when(tokenProvider.getAuthentication(기존_리프레시_토큰)).thenReturn(인증_정보);
            when(tokenProvider.generateToken(인증된_사용자)).thenReturn(예상하는_새_토큰);
            when(stringRedisTemplate.hasKey(anyString())).thenReturn(true);
            when(stringRedisTemplate.delete(anyString())).thenReturn(true);
            when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
            doNothing().when(valueOperations).set(anyString(), eq(""), any(Duration.class));

            // when & then
            assertThatCode(() -> refreshTokenService.refreshJwtTokens(기존_리프레시_토큰))
                    .doesNotThrowAnyException();
        }

        @Test
        void 리프레시_토큰_검증에_실패하면_예외가_발생한다() {
            // given
            String 기존_리프레시_토큰 = "기존 리프레시 토큰";

            when(tokenProvider.validateToken(anyString())).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> refreshTokenService.refreshJwtTokens(기존_리프레시_토큰))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        void 레디스에_리프레시_토큰이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 기존_사용자_ID = 1L;
            String 기존_리프레시_토큰 = "기존 리프레시 토큰";
            SecurityUser 인증된_사용자 = 인증된_사용자는().식별자가(기존_사용자_ID).이다();
            Authentication 인증_정보 = mock(Authentication.class);
            TokenResponse 예상하는_새_토큰 = 발급된_토큰은().액세스_토큰이("새 액세스 토큰").리프레시_토큰이("새 리프레시 토큰").이다();

            when(인증_정보.getPrincipal()).thenReturn(인증된_사용자);
            when(tokenProvider.validateToken(anyString())).thenReturn(true);
            when(tokenProvider.getAuthentication(기존_리프레시_토큰)).thenReturn(인증_정보);
            when(tokenProvider.generateToken(인증된_사용자)).thenReturn(예상하는_새_토큰);
            when(stringRedisTemplate.hasKey(anyString())).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> refreshTokenService.refreshJwtTokens(기존_리프레시_토큰))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
    }
}
