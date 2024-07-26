package com.growup.pms.auth.service;

import static com.growup.pms.test.fixture.auth.RefreshTokenTestBuilder.리프레시_토큰은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.domain.RefreshToken;
import com.growup.pms.auth.repository.RefreshTokenRepository;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.Instant;
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
class RefreshTokenServiceTest {
    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    JwtTokenProvider tokenProvider;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Nested
    class 리프레시_토큰을_저장_시에 {
        @Test
        void 성공한다() {
            // given
            Long 예상_ID = 1L;
            User 기존_사용자 = 사용자는().이다();
            RefreshToken 유효한_리프레시_토큰 = 리프레시_토큰은().사용자가(기존_사용자).이다();

            when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(유효한_리프레시_토큰);

            // when
            Long 실제_ID = refreshTokenService.save(기존_사용자.getId(), 유효한_리프레시_토큰.getToken());

            // then
            assertThat(실제_ID).isEqualTo(예상_ID);
        }

        @Test
        void 존재하지_않는_사용자일_경우_예외가_발생한다() {
            // given
            Long 존재하지_않는_사용자_ID = 1L;
            User 존재하지_않는_사용자 = 사용자는().식별자가(존재하지_않는_사용자_ID).이다();
            String 유효한_리프레시_토큰 = "유효한 리프레시 토큰";

            doThrow(EntityNotFoundException.class).when(userRepository).findByIdOrThrow(존재하지_않는_사용자.getId());

            // when & then
            assertThatThrownBy(() -> refreshTokenService.save(존재하지_않는_사용자_ID, 유효한_리프레시_토큰))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class 리프레시_토큰을_갱신_시에 {
        @Test
        void 기존_토큰이_있으면_갱신한다() {
            // given
            Long 기존_사용자_ID = 1L;
            Long 기존_리프레시_토큰_ID = 1L;
            RefreshToken 기존_리프레시_토큰 = mock(RefreshToken.class);
            String 새_리프레시_토큰 = "새 리프레시 토큰";

            when(refreshTokenRepository.findByUserId(기존_사용자_ID)).thenReturn(Optional.of(기존_리프레시_토큰));
            when(기존_리프레시_토큰.getId()).thenReturn(기존_리프레시_토큰_ID);
            doNothing().when(기존_리프레시_토큰).updateToken(eq(새_리프레시_토큰), any(Instant.class));

            // when
            Long 새_리프레시_토큰_ID = refreshTokenService.renewRefreshToken(기존_사용자_ID, 새_리프레시_토큰);

            // then
            assertThat(새_리프레시_토큰_ID).isEqualTo(기존_리프레시_토큰_ID);
        }

        @Test
        void 기존_토큰이_없으면_새로_저장한다() {
            // given
            Long 기존_사용자_ID = 1L;
            User 기존_사용자 = 사용자는().식별자가(기존_사용자_ID).이다();
            String 새_리프레시_토큰 = "새 리프레시 토큰";
            RefreshToken 변경된_토큰 = 리프레시_토큰은().사용자가(기존_사용자).이다();

            when(refreshTokenRepository.findByUserId(기존_사용자_ID)).thenReturn(Optional.empty());
            when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(변경된_토큰);

            // when
            Long 수정된_토큰_ID = refreshTokenService.renewRefreshToken(기존_사용자_ID, 새_리프레시_토큰);

            // then
            assertThat(수정된_토큰_ID).isEqualTo(변경된_토큰.getId());
        }
    }

    @Nested
    class 리프레시_토큰을_검증_시에 {
        @Test
        void 성공한다() {
            // given
            Long 기존_사용자_ID = 1L;
            User 기존_사용자 = 사용자는().식별자가(기존_사용자_ID).이다();
            Instant 유효한_만료기한 = Instant.now().plusMillis(1000);
            RefreshToken 유효한_토큰 = 리프레시_토큰은().사용자가(기존_사용자).만료기한이(유효한_만료기한).이다();

            when(tokenProvider.validateToken(유효한_토큰.getToken())).thenReturn(true);
            when(tokenProvider.getUserIdFromToken(유효한_토큰.getToken())).thenReturn(기존_사용자_ID);
            when(userRepository.findByIdOrThrow(기존_사용자_ID)).thenReturn(기존_사용자);
            when(refreshTokenRepository.findByUserIdAndToken(eq(기존_사용자_ID), any(String.class))).thenReturn(Optional.of(유효한_토큰));

            // when
            boolean 실제_결과 = refreshTokenService.validateToken(유효한_토큰.getToken());

            // then
            assertThat(실제_결과).isTrue();
        }

        @Test
        void 토큰_검증에_실패하면_실패한다() {
            // given
            String 유효하지_않은_토큰 = "유효하지 않은 토큰";

            when(tokenProvider.validateToken(유효하지_않은_토큰)).thenReturn(false);

            // when
            boolean 실제_결과 = refreshTokenService.validateToken(유효하지_않은_토큰);

            // then
            assertThat(실제_결과).isFalse();
        }

        @Test
        void DB에_해당_토큰이_없을_경우_실패한다() {
            // given
            Long 기존_사용자_ID = 1L;
            User 기존_사용자 = 사용자는().식별자가(기존_사용자_ID).이다();
            String 유효한_리프레시_토큰 = "유효한 리프레시 토큰";

            when(tokenProvider.validateToken(유효한_리프레시_토큰)).thenReturn(true);
            when(tokenProvider.getUserIdFromToken(유효한_리프레시_토큰)).thenReturn(기존_사용자_ID);
            when(userRepository.findByIdOrThrow(기존_사용자_ID)).thenReturn(기존_사용자);
            when(refreshTokenRepository.findByUserIdAndToken(eq(기존_사용자_ID), any(String.class))).thenReturn(Optional.empty());

            // when
            boolean 실제_결과 = refreshTokenService.validateToken(유효한_리프레시_토큰);

            // then
            assertThat(실제_결과).isFalse();
        }

        @Test
        void 토큰이_만료된_경우_실패한다() {
            // given
            Long 기존_사용자_ID = 1L;
            User 기존_사용자 = 사용자는().식별자가(기존_사용자_ID).이다();
            Instant 만료된_만료기한 = Instant.now().minusMillis(1000);
            RefreshToken 만료된_토큰 = 리프레시_토큰은().사용자가(기존_사용자).만료기한이(만료된_만료기한).이다();

            when(tokenProvider.validateToken(만료된_토큰.getToken())).thenReturn(true);
            when(tokenProvider.getUserIdFromToken(만료된_토큰.getToken())).thenReturn(기존_사용자_ID);
            when(userRepository.findByIdOrThrow(기존_사용자_ID)).thenReturn(기존_사용자);
            when(refreshTokenRepository.findByUserIdAndToken(eq(기존_사용자_ID), any(String.class))).thenReturn(Optional.of(만료된_토큰));

            // when
            boolean 실제_결과 = refreshTokenService.validateToken(만료된_토큰.getToken());

            // then
            assertThat(실제_결과).isFalse();
        }
    }
}
