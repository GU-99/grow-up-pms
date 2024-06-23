package com.growup.pms.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.user.UserCreateRequestFixture;
import com.growup.pms.test.fixture.user.UserFixture;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.dto.UserCreateRequest;
import com.growup.pms.user.repository.UserRepository;
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

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    class 사용자가 {

        @Test
        void 성공적으로_계정을_생성한다() {
            // given
            Long expectedUserId = 1L;
            User user = UserFixture.createUserWithId(expectedUserId);
            UserCreateRequest request = UserCreateRequestFixture.createRequestFromUser(user);

            when(userRepository.save(any(User.class))).thenReturn(user);

            // when
            Long actualUserId = userService.save(request);

            // then
            assertThat(actualUserId).isEqualTo(expectedUserId);
        }

        @Test
        void 중복된_아이디를_사용하면_예외가_발생한다() {
            // given
            User user = UserFixture.createDefaultUserBuilder().build();
            UserCreateRequest request = UserCreateRequestFixture.createRequestFromUser(user);

            doThrow(DataIntegrityViolationException.class).when(userRepository).save(any(User.class));

            // when & then
            assertThatThrownBy(() -> userService.save(request))
                    .isInstanceOf(DuplicateException.class);
        }
    }
}
