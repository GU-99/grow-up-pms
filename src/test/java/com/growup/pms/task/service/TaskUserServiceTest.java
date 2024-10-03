package com.growup.pms.task.service;

import static com.growup.pms.test.fixture.task.builder.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.task.repository.TaskUserRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TaskUserServiceTest {

    @Mock
    private TaskUserRepository taskUserRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskUserService taskUserService;

    @Nested
    class 프로젝트_일정_수행자_추가시에 {

        @Test
        void 성공한다() {
            // given
            Long 예상_일정_ID = 1L;
            Long 예상_회원_ID = 1L;
            Task 기존_일정 = 일정은().이다();
            User 기존_회원 = 사용자는().이다();

            when(taskRepository.findByIdOrThrow(예상_일정_ID)).thenReturn(기존_일정);
            when(userRepository.findByIdOrThrow(예상_회원_ID)).thenReturn(기존_회원);

            // when & then
            assertThatCode(() -> taskUserService.createTaskUser(예상_일정_ID, 예상_회원_ID))
                    .doesNotThrowAnyException();
        }

        @Test
        void 일정이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 잘못된_일정_ID = Long.MIN_VALUE;
            Long 예상_회원_ID = 1L;

            doThrow(BusinessException.class).when(taskRepository).findByIdOrThrow(잘못된_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskUserService.createTaskUser(잘못된_일정_ID, 예상_회원_ID))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void 회원이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 예상_일정_ID = Long.MIN_VALUE;
            Long 잘못된_회원_ID = 1L;
            Task 기존_일정 = 일정은().이다();

            when(taskRepository.findByIdOrThrow(예상_일정_ID)).thenReturn(기존_일정);
            doThrow(BusinessException.class).when(userRepository).findByIdOrThrow(잘못된_회원_ID);

            // when & then
            assertThatThrownBy(() -> taskUserService.createTaskUser(예상_일정_ID, 잘못된_회원_ID))
                    .isInstanceOf(BusinessException.class);
        }
    }
}
