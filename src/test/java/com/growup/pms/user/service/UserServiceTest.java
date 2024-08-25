package com.growup.pms.user.service;

import static com.growup.pms.test.fixture.user.UserCreateRequestTestBuilder.가입하는_사용자는;
import static com.growup.pms.test.fixture.user.UserSearchResponseTestBuilder.사용자_검색_응답은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.service.EmailVerificationService;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import com.growup.pms.user.service.dto.UserCreateCommand;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    EmailVerificationService emailVerificationService;

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    class 사용자가_회원가입_시에 {
        @Test
        void 성공적으로_계정을_생성한다() {
            // given
            Long 예상하는_새_사용자_ID = 1L;
            User 새_사용자 = 사용자는().식별자가(예상하는_새_사용자_ID).이다();
            UserCreateCommand 사용자_생성_요청 = 가입하는_사용자는(새_사용자).이다().toCommand();

            when(emailVerificationService.verifyAndInvalidateEmail(eq(새_사용자.getEmail()), anyString())).thenReturn(true);
            when(userRepository.save(any(User.class))).thenReturn(새_사용자);

            // when
            Long 실제_새_사용자_ID = userService.save(사용자_생성_요청);

            // then
            assertThat(실제_새_사용자_ID).isEqualTo(예상하는_새_사용자_ID);
        }

        @Test
        void 중복된_아이디를_사용하면_예외가_발생한다() {
            // given
            User 새_사용자 = 사용자는().이메일이("중복된 이메일").이다();
            UserCreateCommand 사용자_생성_요청 = 가입하는_사용자는(새_사용자).이다().toCommand();

            when(emailVerificationService.verifyAndInvalidateEmail(eq(새_사용자.getEmail()), anyString())).thenReturn(true);
            doThrow(DataIntegrityViolationException.class).when(userRepository).save(any(User.class));

            // when & then
            assertThatThrownBy(() -> userService.save(사용자_생성_요청))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void 이메일_인증에_실패하면_예외가_발생한다() {
            // given
            User 새_사용자 = 사용자는().이다();
            UserCreateCommand 사용자_생성_요청 = 가입하는_사용자는(새_사용자).이다().toCommand();

            when(emailVerificationService.verifyAndInvalidateEmail(anyString(), anyString())).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> userService.save(사용자_생성_요청))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
        }
    }

    @Nested
    class 전체_사용자_검색_시에 {
        @Test
        void 성공한다() {
            // given
            String 닉네임_접두사 = "브";
            List<UserSearchResponse> 예상_결과 = List.of(사용자_검색_응답은().닉네임이("브라운").이다());

            when(userRepository.findUsersByNicknameStartingWith(닉네임_접두사)).thenReturn(예상_결과);

            // when
            List<UserSearchResponse> 실제_결과 = userService.searchUsersByNicknamePrefix(닉네임_접두사);

            // then
            assertThat(실제_결과).isEqualTo(예상_결과);
        }
    }
}
