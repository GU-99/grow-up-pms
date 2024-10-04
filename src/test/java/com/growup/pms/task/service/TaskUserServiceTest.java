package com.growup.pms.task.service;

import static com.growup.pms.test.fixture.task.builder.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.task.builder.TaskUserResponseTestBuilder.일정_수행자_목록_응답은;
import static com.growup.pms.test.fixture.task.builder.TaskUserTestBuilder.일정_수행자는;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.role.domain.ProjectRole;
import com.growup.pms.task.controller.dto.response.TaskUserResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.domain.TaskUser;
import com.growup.pms.task.domain.TaskUserId;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.task.repository.TaskUserRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
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

    @Nested
    class 프로젝트_일정_수행자_목록_조회시 {

        @Test
        void 성공한다() {
            // given
            Long 프로젝트_ID = 1L;
            Long 일정_ID = 2L;
            TaskUserResponse 예상_결과_항목_1 = 일정_수행자_목록_응답은()
                    .회원_식별자가(1L)
                    .닉네임이("브라운")
                    .역할_이름이(ProjectRole.ADMIN.getRoleName())
                    .이다();
            TaskUserResponse 예상_결과_항목_2 = 일정_수행자_목록_응답은()
                    .회원_식별자가(2L)
                    .닉네임이("코니")
                    .역할_이름이(ProjectRole.LEADER.getRoleName())
                    .이다();
            List<TaskUserResponse> 예상_결과 = List.of(예상_결과_항목_1, 예상_결과_항목_2);

            when(taskUserRepository.getTaskUsersByProjectIdAndTaskId(anyLong(), anyLong()))
                    .thenReturn(예상_결과);

            // when
            List<TaskUserResponse> 실제_결과 = taskUserService.getAssignees(프로젝트_ID, 일정_ID);

            // then
            assertThat(실제_결과).hasSize(예상_결과.size())
                    .extracting("userId", "nickname", "roleName")
                    .containsExactlyInAnyOrder(
                            tuple(예상_결과_항목_1.userId(), 예상_결과_항목_1.nickname(), 예상_결과_항목_1.roleName()),
                            tuple(예상_결과_항목_2.userId(), 예상_결과_항목_2.nickname(), 예상_결과_항목_2.roleName())
                    );
        }

        @Test
        void 프로젝트_식별자가_NULL_이면_예외가_발생한다() {
            // given
            Long 프로젝트_ID = null;
            Long 일정_ID = 2L;

            doThrow(BusinessException.class).when(taskUserRepository)
                    .getTaskUsersByProjectIdAndTaskId(nullable(Long.class), anyLong());

            // when & then
            assertThatThrownBy(() -> taskUserService.getAssignees(프로젝트_ID, 일정_ID))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void 프로젝트나_일정이_없으면_빈_리스트를_반환한다() {
            // given
            Long 잘못된_프로젝트_ID = Long.MIN_VALUE;
            Long 일정_ID = 1L;

            Long 프로젝트_ID = 1L;
            Long 잘못된_일정_ID = Long.MIN_VALUE;

            // when
            List<TaskUserResponse> 잘못된_프로젝트_ID_결과 = taskUserService.getAssignees(잘못된_프로젝트_ID, 일정_ID);
            List<TaskUserResponse> 잘못된_일정_ID_결과 = taskUserService.getAssignees(프로젝트_ID, 잘못된_일정_ID);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(잘못된_프로젝트_ID_결과).isEmpty();
                softly.assertThat(잘못된_일정_ID_결과).isEmpty();
            });
        }
    }

    @Nested
    class 프로젝트_일정_수행자_삭제시 {

        @Test
        void 성공한다() {
            // given
            Long 잘못된_일정_ID = 1L;
            Long 잘못된_회원_ID = 1L;
            TaskUser 기존_수행자 = 일정_수행자는().이다();
            when(taskUserRepository.findByIdOrThrow(any(TaskUserId.class))).thenReturn(기존_수행자);

            // when
            taskUserService.deleteTaskUser(잘못된_일정_ID, 잘못된_회원_ID);

            // then
            verify(taskUserRepository).delete(기존_수행자);
        }

        @Test
        void 해당_수행자가_없으면_예외가_발생한다() {
            // given
            Long 잘못된_일정_ID = Long.MIN_VALUE;
            Long 잘못된_회원_ID = Long.MIN_VALUE;
            doThrow(BusinessException.class).when(taskUserRepository).findByIdOrThrow(any(TaskUserId.class));

            // when & then
            assertThatThrownBy(() -> taskUserService.deleteTaskUser(잘못된_일정_ID, 잘못된_회원_ID))
                    .isInstanceOf(BusinessException.class);
        }
    }
}
