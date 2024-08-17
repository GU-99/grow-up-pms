package com.growup.pms.task.service;

import static com.growup.pms.test.fixture.status.StatusTestBuilder.상태는;
import static com.growup.pms.test.fixture.task.TaskCreateRequestTestBuilder.일정_생성_요청은;
import static com.growup.pms.test.fixture.task.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.task.service.dto.TaskCreateCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
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
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    StatusRepository statusRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TaskService taskService;

    @Nested
    class 사용자가_프로젝트_일정_생성시에 {

        @Test
        void 성공한다() {
            // given
            Long taskId = 1L;
            Long statusId = 1L;
            Long userId = 1L;
            Status status = 상태는().식별자가(statusId).이다();
            User user = 사용자는().식별자가(userId).이다();
            Task task = 일정은().이다();
            TaskCreateCommand command = 일정_생성_요청은().이다().toCommand();

            when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(taskRepository.save(any(Task.class))).thenReturn(task);

            // when
            TaskDetailResponse response = taskService.createTask(command);

            // then
            assertThat(response.getTaskId()).isEqualTo(taskId);
        }

        @Test
        void 담당회원과_상태가_없어도_성공한다() {
            Long taskId = 1L;
            Long statusId = 1L;
            Long userId = 1L;
            Task task = 일정은().회원은(null).상태는(null).이다();
            TaskCreateCommand command = 일정_생성_요청은().이다().toCommand();

            when(statusRepository.findById(statusId)).thenReturn(Optional.empty());
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            when(taskRepository.save(any(Task.class))).thenReturn(task);

            // when
            TaskDetailResponse response = taskService.createTask(command);

            // then
            assertThat(response.getTaskId()).isEqualTo(taskId);
            assertThat(response.getStatusId()).isNull();
            assertThat(response.getUserNickname()).isNull();
        }
    }
}
