package com.growup.pms.user.service;

import static com.growup.pms.test.fixture.user.RecoverPasswordRequestTestBuilder.비밀번호_찾기_요청은;
import static com.growup.pms.test.fixture.user.RecoverUsernameRequestTestBuilder.아이디_찾기_요청은;
import static com.growup.pms.test.fixture.user.UserCreateRequestTestBuilder.가입하는_사용자는;
import static com.growup.pms.test.fixture.user.UserPasswordUpdateTestBuilder.비밀번호_변경은;
import static com.growup.pms.test.fixture.user.UserSearchResponseTestBuilder.사용자_검색_응답은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static com.growup.pms.test.fixture.user.UserUpdateRequestTestBuilder.사용자_정보_변경_요청은;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.service.RedisEmailVerificationService;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.controller.dto.response.RecoverPasswordResponse;
import com.growup.pms.user.controller.dto.response.RecoverUsernameResponse;
import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.controller.dto.response.UserUpdateResponse;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import com.growup.pms.user.service.dto.PasswordUpdateCommand;
import com.growup.pms.user.service.dto.RecoverPasswordCommand;
import com.growup.pms.user.service.dto.RecoverUsernameCommand;
import com.growup.pms.user.service.dto.UserCreateCommand;
import com.growup.pms.user.service.dto.UserUpdateCommand;
import java.time.LocalDateTime;
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
    RedisEmailVerificationService emailVerificationService;

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

    @Nested
    class 비밀번호_변경_시에 {

        @Test
        void 성공적으로_비밀번호를_변경한다() {
            // given
            String 기존_비밀번호 = "test1234!@#$";
            String 새로운_비밀번호 = "test2345!@#$";
            User 기존_사용자 = 사용자는().이다();
            LocalDateTime 기존_비밀번호_수정일 = 기존_사용자.getPasswordChangeDate();

            PasswordUpdateCommand 비밀번호_변경_요청 = 비밀번호_변경은().기존_비밀번호가(기존_비밀번호).새로운_비밀번호가(새로운_비밀번호).이다().toCommand();

            when(userRepository.findByIdOrThrow(기존_사용자.getId())).thenReturn(기존_사용자);
            when(passwordEncoder.matches(비밀번호_변경_요청.password(), 기존_사용자.getPassword())).thenReturn(true);

            // when
            userService.updatePassword(기존_사용자.getId(), 비밀번호_변경_요청);

            // then
            assertThat(기존_비밀번호_수정일).isNotEqualTo(기존_사용자.getPasswordChangeDate());
        }

        @Test
        void 저장된_비밀번호와_매치에_실패하면_예외가_발생한다() {
            // given
            String 기존_비밀번호 = "test1234!@#$";
            String 새로운_비밀번호 = "test2345!@#$";
            User 기존_사용자 = 사용자는().이다();
            PasswordUpdateCommand 비밀번호_변경_요청 = 비밀번호_변경은().기존_비밀번호가(기존_비밀번호).새로운_비밀번호가(새로운_비밀번호).이다().toCommand();

            when(userRepository.findByIdOrThrow(기존_사용자.getId())).thenReturn(기존_사용자);
            when(passwordEncoder.matches(비밀번호_변경_요청.password(), 기존_사용자.getPassword())).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> userService.updatePassword(기존_사용자.getId(), 비밀번호_변경_요청))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_PASSWORD);
        }
    }

    @Nested
    class 유저_정보_변경_시에 {

        @Test
        void 성공한다() {
            // given
            User 기존_사용자 = 사용자는().이다();
            String 변경할_닉네임 = "wlshooo";
            String 변경할_자기소개 = "신입입니다. 잘 부탁드려요!";
            String 변경할_프로필_이미지_URL = "http://example.com/profile.png";
            List<String> 링크 = List.of("http://github.com", "http://blog.example.com");

            UserUpdateCommand 사용자_정보_변경_요청 = 사용자_정보_변경_요청은()
                    .닉네임이(변경할_닉네임).자기소개는(변경할_자기소개).프로필_이미지_URL이(변경할_프로필_이미지_URL).링크가(링크).이다().toCommand();

            when(userRepository.findByIdOrThrow(기존_사용자.getId())).thenReturn(기존_사용자);

            // when
            UserUpdateResponse 변경된_유저_정보 = userService.updateUserDetails(기존_사용자.getId(), 사용자_정보_변경_요청);

            // then
            assertSoftly(softly -> {
                softly.assertThat(변경된_유저_정보.links()).hasSize(2);
                softly.assertThat(변경된_유저_정보)
                        .extracting("userId", "nickname", "profileImageUrl", "bio", "links")
                        .contains(1L, 변경할_닉네임, 변경할_자기소개, 변경할_프로필_이미지_URL, 링크);
            });
        }

        @Test
        void 링크가_5개를_초과하면_예외가_발생한다() {
            // given
            User 기존_사용자 = 사용자는().이다();
            List<String> 링크 = List.of("http://github.com", "http://blog.example.com",
                    "http://GU-99.com", "http://longBright.com", "http://yachimiy.com", "http://wlshooo.com");

            UserUpdateCommand 사용자_정보_변경_요청 = 사용자_정보_변경_요청은().링크가(링크).이다().toCommand();

            when(userRepository.findByIdOrThrow(기존_사용자.getId())).thenReturn(기존_사용자);

            // when & then
            assertThatThrownBy(() -> userService.updateUserDetails(기존_사용자.getId(), 사용자_정보_변경_요청))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("더 이상 링크를 등록할 수 없습니다.");
        }

        @Nested
        class 아이디_찾기_시 {

            @Test
            void 성공한다() {
                // given
                String 연결된_이메일 = "brown@example.com";
                User 잃어버린_계정 = 사용자는().이메일이(연결된_이메일).이다();
                RecoverUsernameCommand 아이디_찾기_요청 = 아이디_찾기_요청은().이메일이(연결된_이메일).이다().toCommand();

                when(emailVerificationService.verifyAndInvalidateEmail(연결된_이메일,
                        String.valueOf(아이디_찾기_요청.verificationCode()))).thenReturn(true);
                when(userRepository.findByEmailOrThrow(연결된_이메일)).thenReturn(잃어버린_계정);

                // when
                RecoverUsernameResponse 실제_결과 = userService.recoverUsername(아이디_찾기_요청);

                // then
                assertThat(실제_결과.username()).isEqualTo(잃어버린_계정.getUsername());
            }

            @Test
            void 이메일_인증에_실패하면_예외가_발생한다() {
                // given
                String 연결된_이메일 = "brown@example.com";
                RecoverUsernameCommand 아이디_찾기_요청 = 아이디_찾기_요청은().이메일이(연결된_이메일).이다().toCommand();

                when(emailVerificationService.verifyAndInvalidateEmail(연결된_이메일,
                        String.valueOf(아이디_찾기_요청.verificationCode()))).thenReturn(false);

                // when & then
                assertThatThrownBy(() -> userService.recoverUsername(아이디_찾기_요청))
                        .isInstanceOf(BusinessException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
            }
        }

        @Nested
        class 비밀번호_찾기_시 {

            @Test
            void 성공한다() {
                // given
                String 연결된_이메일 = "brown@example.com";
                String 연결된_아이디 = "brown";
                User 잃어버린_계정 = 사용자는().이메일이(연결된_이메일).아이디가(연결된_아이디).이다();
                RecoverPasswordCommand 비밀번호_찾기_요청 = 비밀번호_찾기_요청은().이메일이(연결된_이메일).아이디가(연결된_아이디).이다().toCommand();

                when(emailVerificationService.verifyAndInvalidateEmail(연결된_이메일,
                        String.valueOf(비밀번호_찾기_요청.verificationCode()))).thenReturn(true);
                when(userRepository.findByEmailOrThrow(연결된_이메일)).thenReturn(잃어버린_계정);

                // when
                RecoverPasswordResponse 실제_결과 = userService.recoverPassword(비밀번호_찾기_요청);

                // then
                assertThat(실제_결과.password()).isNotEmpty();
            }

            @Test
            void 이메일_인증에_실패하면_예외가_발생한다() {
                // given
                String 연결된_이메일 = "brown@example.com";
                String 연결된_아이디 = "brown";
                RecoverPasswordCommand 비밀번호_찾기_요청 = 비밀번호_찾기_요청은().이메일이(연결된_이메일).아이디가(연결된_아이디).이다().toCommand();

                when(emailVerificationService.verifyAndInvalidateEmail(연결된_이메일,
                        String.valueOf(비밀번호_찾기_요청.verificationCode()))).thenReturn(false);

                // when & then
                assertThatThrownBy(() -> userService.recoverPassword(비밀번호_찾기_요청))
                        .isInstanceOf(BusinessException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
            }

            @Test
            void 사용자_아이디가_일치하지_않으면_예외가_발생한다() {
                // given
                String 연결된_이메일 = "brown@example.com";
                String 연결된_아이디 = "brown";
                String 입력된_아이디 = "incorrect_username";
                User 잃어버린_계정 = 사용자는().이메일이(연결된_이메일).아이디가(연결된_아이디).이다();
                RecoverPasswordCommand 비밀번호_찾기_요청 = 비밀번호_찾기_요청은().이메일이(연결된_이메일).아이디가(입력된_아이디).이다().toCommand();

                when(emailVerificationService.verifyAndInvalidateEmail(연결된_이메일,
                        String.valueOf(비밀번호_찾기_요청.verificationCode()))).thenReturn(true);
                when(userRepository.findByEmailOrThrow(연결된_이메일)).thenReturn(잃어버린_계정);

                // when & then
                assertThatThrownBy(() -> userService.recoverPassword(비밀번호_찾기_요청))
                        .isInstanceOf(BusinessException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

            }
        }
    }
}
