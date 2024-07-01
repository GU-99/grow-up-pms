package com.growup.pms.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.domain.RefreshToken;
import com.growup.pms.auth.repository.RefreshTokenRepository;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.auth.RefreshTokenFixture;
import com.growup.pms.test.fixture.user.UserFixture;
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
            Long expectedId = 1L;
            User user = UserFixture.createUserWithId(1L);
            RefreshToken validRefreshToken = RefreshTokenFixture.createRefreshTokenWithUser(user);

            when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(validRefreshToken);

            // when
            Long actualId = refreshTokenService.save(user.getId(), validRefreshToken.getToken());

            // then
            assertThat(actualId).isEqualTo(expectedId);

            verify(refreshTokenRepository).save(any(RefreshToken.class));
        }

        @Test
        void 존재하지_않는_유저일_경우_예외가_발생한다() {
            // given
            Long userId = 1L;
            User user = UserFixture.createUserWithId(userId);

            doThrow(EntityNotFoundException.class).when(userRepository).findByIdOrThrow(user.getId());

            // when & then
            assertThatThrownBy(() -> refreshTokenService.save(userId, RefreshTokenFixture.VALID_REFRESH_TOKEN))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class 리프레시_토큰을_제거_시에 {

        @Test
        void 성공한다() {
            // given
            User user = UserFixture.createUserWithId(1L);

            // when
            refreshTokenService.deleteTokenByUserId(user.getId());

            // then
            verify(refreshTokenRepository).deleteById(user.getId());
            verify(refreshTokenRepository).flush();
        }
    }

    @Nested
    class 리프레시_토큰을_검증_시에 {

        @Test
        void 성공한다() {
            // given
            Long userId = 1L;
            User user = UserFixture.createUserWithId(userId);
            Instant validExpiryDate = Instant.now().plusMillis(1000);
            RefreshToken validRefreshToken = RefreshTokenFixture.createRefreshTokenWithUserAndExpiryDate(user, validExpiryDate);

            when(tokenProvider.validateToken(validRefreshToken.getToken())).thenReturn(true);
            when(tokenProvider.getUserIdFromToken(validRefreshToken.getToken())).thenReturn(userId);
            when(userRepository.findByIdOrThrow(userId)).thenReturn(user);
            when(refreshTokenRepository.findByUserIdAndToken(eq(userId), any(String.class))).thenReturn(Optional.of(validRefreshToken));

            // when
            boolean actualResult = refreshTokenService.validateToken(validRefreshToken.getToken());

            // then
            assertThat(actualResult).isTrue();
        }

        @Test
        void 토큰_검증에_실패하면_실패한다() {
            // given
            String invalidToken = RefreshTokenFixture.INVALID_REFRESH_TOKEN;

            when(tokenProvider.validateToken(invalidToken)).thenReturn(false);

            // when
            boolean actualResult = refreshTokenService.validateToken(invalidToken);

            // then
            assertThat(actualResult).isFalse();
        }

        @Test
        void DB에_해당_토큰이_없을_경우_실패한다() {
            // given
            Long userId = 1L;
            User user = UserFixture.createUserWithId(userId);
            String validRefreshToken = RefreshTokenFixture.VALID_REFRESH_TOKEN;

            when(tokenProvider.validateToken(validRefreshToken)).thenReturn(true);
            when(tokenProvider.getUserIdFromToken(validRefreshToken)).thenReturn(userId);
            when(userRepository.findByIdOrThrow(userId)).thenReturn(user);
            when(refreshTokenRepository.findByUserIdAndToken(eq(userId), any(String.class))).thenReturn(Optional.empty());

            // when
            boolean actualResult = refreshTokenService.validateToken(validRefreshToken);

            // then
            assertThat(actualResult).isFalse();
        }

        @Test
        void 토큰이_만료된_경우_실패한다() {
            // given
            Long userId = 1L;
            User user = UserFixture.createUserWithId(userId);
            Instant expiredExpiryDate = Instant.now().minusMillis(1000);
            RefreshToken expiredRefreshToken = RefreshTokenFixture.createRefreshTokenWithUserAndExpiryDate(user, expiredExpiryDate);

            when(tokenProvider.validateToken(expiredRefreshToken.getToken())).thenReturn(true);
            when(tokenProvider.getUserIdFromToken(expiredRefreshToken.getToken())).thenReturn(userId);
            when(userRepository.findByIdOrThrow(userId)).thenReturn(user);
            when(refreshTokenRepository.findByUserIdAndToken(eq(userId), any(String.class))).thenReturn(Optional.of(expiredRefreshToken));

            // when
            boolean actualResult = refreshTokenService.validateToken(expiredRefreshToken.getToken());

            // then
            assertThat(actualResult).isFalse();
        }
    }
}
