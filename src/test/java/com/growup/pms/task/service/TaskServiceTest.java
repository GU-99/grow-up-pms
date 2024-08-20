package com.growup.pms.task.service;

import static com.growup.pms.test.fixture.status.StatusTestBuilder.상태는;
import static com.growup.pms.test.fixture.task.TaskCreateRequestTestBuilder.일정_생성_요청은;
import static com.growup.pms.test.fixture.task.TaskDetailResponseTestBuilder.일정_상세조회_응답은;
import static com.growup.pms.test.fixture.task.TaskEditRequestTestBuilder.일정_수정_요청은;
import static com.growup.pms.test.fixture.task.TaskResponseTestBuilder.일정_전체조회_응답은;
import static com.growup.pms.test.fixture.task.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.task.service.dto.TaskCreateCommand;
import com.growup.pms.task.service.dto.TaskEditCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
            Long 예상_일정_ID = 1L;
            Long 예상_프로젝트_ID = 1L;
            Long 예상_상태_ID = 1L;
            Long 예상_담당자_ID = 1L;
            Status 예상_상태 = 상태는().식별자가(예상_상태_ID).이다();
            User 예상_담당자 = 사용자는().식별자가(예상_담당자_ID).이다();
            Task 예상_일정 = 일정은().이다();
            TaskCreateCommand 예상_일정_생성_요청 = 일정_생성_요청은().이다().toCommand();

            when(statusRepository.findById(예상_상태_ID)).thenReturn(Optional.of(예상_상태));
            when(userRepository.findById(예상_담당자_ID)).thenReturn(Optional.of(예상_담당자));
            when(taskRepository.save(any(Task.class))).thenReturn(예상_일정);

            // when
            TaskDetailResponse 실제_결과 = taskService.createTask(예상_프로젝트_ID, 예상_일정_생성_요청);

            // then
            assertThat(실제_결과.getTaskId()).isEqualTo(예상_일정_ID);
        }

        @Test
        void 담당회원과_상태가_없어도_성공한다() {
            Long 예상_일정_ID = 1L;
            Long 예상_프로젝트_ID = 1L;
            Long 예상_상태_ID = 1L;
            Long 예상_담당자_ID = 1L;
            Task 예상_일정 = 일정은().회원은(null).상태는(null).이다();
            TaskCreateCommand 예상_일정_생성_요청 = 일정_생성_요청은().이다().toCommand();

            when(statusRepository.findById(예상_상태_ID)).thenReturn(Optional.empty());
            when(userRepository.findById(예상_담당자_ID)).thenReturn(Optional.empty());
            when(taskRepository.save(any(Task.class))).thenReturn(예상_일정);

            // when
            TaskDetailResponse 실제_결과 = taskService.createTask(예상_프로젝트_ID, 예상_일정_생성_요청);

            // then
            assertThat(실제_결과.getTaskId()).isEqualTo(예상_일정_ID);
            assertThat(실제_결과.getStatusId()).isNull();
            assertThat(실제_결과.getUserNickname()).isNull();
        }
    }

    @Nested
    class 사용자가_상태별로_프로젝트_일정_전체_조회시에 {

        @Test
        void 성공한다() {
            // given
            Long 예상_프로젝트_ID = 1L;
            Long 예상_상태_ID_1 = 1L;
            Long 예상_상태_ID_2 = 2L;

            TaskResponse 예상_일정_1 = 일정_전체조회_응답은()
                    .일정_식별자는(1L)
                    .상태_식별자는(예상_상태_ID_1)
                    .회원_닉네임은("브라운")
                    .일정이름은("PMS 프로젝트의 환경설정을 진행함")
                    .정렬순서는((short) 1)
                    .이다();

            TaskResponse 예상_일정_2 = 일정_전체조회_응답은()
                    .일정_식별자는(2L)
                    .상태_식별자는(예상_상태_ID_1)
                    .회원_닉네임은("레니")
                    .일정이름은("PMS 프로젝트의 등록 기능 구현을 진행함")
                    .정렬순서는((short) 2)
                    .이다();

            TaskResponse 예상_일정_3 = 일정_전체조회_응답은()
                    .일정_식별자는(3L)
                    .상태_식별자는(예상_상태_ID_2)
                    .회원_닉네임은("브라운")
                    .일정이름은("PMS 프로젝트의 조회 기능 구현을 진행함")
                    .정렬순서는((short) 3)
                    .이다();

            List<TaskResponse> 예상_일정_목록_1 = List.of(예상_일정_1, 예상_일정_2);
            List<TaskResponse> 예상_일정_목록_2 = List.of(예상_일정_3);

            Map<Long, List<TaskResponse>> 예상_결과 = Map.of(예상_상태_ID_1, 예상_일정_목록_1, 예상_상태_ID_2, 예상_일정_목록_2);

            when(taskRepository.getTasksByProjectId(예상_프로젝트_ID)).thenReturn(예상_결과);

            // when
            Map<Long, List<TaskResponse>> 실제_결과 = taskService.getTasks(예상_프로젝트_ID);

            // then
            assertThat(실제_결과).hasSize(2);
            assertThat(실제_결과.get(예상_상태_ID_1)).hasSize(2);
            assertThat(실제_결과.get(예상_상태_ID_1).stream().map(TaskResponse::taskName))
                    .containsExactlyInAnyOrder("PMS 프로젝트의 환경설정을 진행함", "PMS 프로젝트의 등록 기능 구현을 진행함");
            assertThat(실제_결과.get(예상_상태_ID_2)).hasSize(1);
            assertThat(실제_결과.get(예상_상태_ID_2).stream().map(TaskResponse::taskName))
                    .containsExactlyInAnyOrder("PMS 프로젝트의 조회 기능 구현을 진행함");
        }

        @Test
        void 해당_프로젝트에_일정이_없으면_빈맵을_반환한다() {
            // given
            Long 잘못된_프로젝트_ID = Long.MAX_VALUE;

            Map<Long, List<TaskResponse>> 예상_결과 = Collections.emptyMap();
            when(taskRepository.getTasksByProjectId(잘못된_프로젝트_ID)).thenReturn(예상_결과);

            // when
            Map<Long, List<TaskResponse>> 실제_결과 = taskService.getTasks(잘못된_프로젝트_ID);

            // then
            assertThat(실제_결과).isEmpty();
        }
    }

    @Nested
    class 사용자가_프로젝트_일정_상세_조회시에 {

        @Test
        void 성공한다() {
            // given
            Long 예상_일정_ID = 1L;
            Task 기존_일정 = 일정은().식별자는(예상_일정_ID).이다();
            TaskDetailResponse 예상_응답 = 일정_상세조회_응답은().일정_식별자는(예상_일정_ID).이다();

            when(taskRepository.findByIdOrThrow(예상_일정_ID)).thenReturn(기존_일정);

            // when
            TaskDetailResponse 실제_결과 = taskService.getTask(예상_일정_ID);

            // then
            assertThat(실제_결과.getTaskId()).isEqualTo(예상_응답.getTaskId());
        }

        @Test
        void 일정이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 존재하지_않는_일정_ID = 1L;

            doThrow(new EntityNotFoundException(ErrorCode.TASK_NOT_FOUND))
                    .when(taskRepository).findByIdOrThrow(존재하지_않는_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskService.getTask(존재하지_않는_일정_ID))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("존재하지 않는 프로젝트 일정입니다.");
        }
    }

    @Nested
    class 사용자가_프로젝트_일정_변경시에 {

        @Test
        void 성공한다() {
            // given
            Long 기존_일정_ID = 1L;
            Task 기존_일정 = 일정은().식별자는(기존_일정_ID).이다();
            Long 변경할_담당자_ID = 2L;
            User 변경할_담당자 = 사용자는().식별자가(변경할_담당자_ID).이다();
            Long 변경할_상태_ID = 2L;
            Status 변경할_상태 = 상태는().식별자가(변경할_상태_ID).이다();
            String 변경할_일정_이름 = "변경할 이름 입니다!";
            String 변경할_내용 = "변경할 내용!!!!!!!!!!#@#%^#$^&*%(^*&(^%$#231382304-2315982ㅅ89asdfjlaiejvlsakc";
            Short 변경할_정렬순서 = (short) 2;
            LocalDate 변경할_시작일자 = LocalDate.of(2023, 1, 1);
            LocalDate 변경할_종료일자 = LocalDate.of(2023, 3, 1);
            TaskEditCommand 일정_변경_요청 = 일정_수정_요청은()
                    .회원_식별자는(변경할_담당자_ID)
                    .상태_식별자는(변경할_상태_ID)
                    .일정이름은(변경할_일정_이름)
                    .본문내용은(변경할_내용)
                    .정렬순서는(변경할_정렬순서)
                    .시작일자는(변경할_시작일자)
                    .종료일자는(변경할_종료일자)
                    .이다().toCommand();

            when(taskRepository.findByIdOrThrow(기존_일정_ID)).thenReturn(기존_일정);
            when(userRepository.findByIdOrThrow(변경할_담당자_ID)).thenReturn(변경할_담당자);
            when(statusRepository.findByIdOrThrow(변경할_상태_ID)).thenReturn(변경할_상태);

            // when
            taskService.editTask(기존_일정_ID, 일정_변경_요청);

            // then
            assertThat(기존_일정.getStatus().getId()).isEqualTo(변경할_상태_ID);
            assertThat(기존_일정.getUser().getId()).isEqualTo(변경할_담당자_ID);
            assertThat(기존_일정.getName()).isEqualTo(변경할_일정_이름);
            assertThat(기존_일정.getContent()).isEqualTo(변경할_내용);
            assertThat(기존_일정.getSortOrder()).isEqualTo(변경할_정렬순서);
            assertThat(기존_일정.getStartDate()).isEqualTo(변경할_시작일자);
            assertThat(기존_일정.getEndDate()).isEqualTo(변경할_종료일자);
        }

        @Test
        void 일정이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 잘못된_일정_ID = 1L;
            TaskEditCommand 일정_변경_요청 = 일정_수정_요청은().이다().toCommand();

            doThrow(new EntityNotFoundException(ErrorCode.TASK_NOT_FOUND))
                    .when(taskRepository).findByIdOrThrow(잘못된_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskService.editTask(잘못된_일정_ID, 일정_변경_요청))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("존재하지 않는 프로젝트 일정입니다.");
        }

        @Test
        void 변경하려는_회원이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 기존_일정_ID = 1L;
            Task 기존_일정 = 일정은().식별자는(기존_일정_ID).이다();
            Long 잘못된_담당자_ID = 2L;
            TaskEditCommand 일정_변경_요청 = 일정_수정_요청은().회원_식별자는(잘못된_담당자_ID).이다().toCommand();

            when(taskRepository.findByIdOrThrow(기존_일정_ID)).thenReturn(기존_일정);
            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND))
                    .when(userRepository).findByIdOrThrow(잘못된_담당자_ID);

            // when & then
            assertThatThrownBy(() -> taskService.editTask(기존_일정_ID, 일정_변경_요청))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("ENTITY가 없습니다.");
        }

        @Test
        void 변경하려는_상태가_존재하지_않으면_예외가_발생한다() {
            // given
            Long 기존_일정_ID = 1L;
            Task 기존_일정 = 일정은().식별자는(기존_일정_ID).이다();
            Long 잘못된_상태_ID = 2L;
            TaskEditCommand 일정_변경_요청 = 일정_수정_요청은().상태_식별자는(잘못된_상태_ID).이다().toCommand();

            when(taskRepository.findByIdOrThrow(기존_일정_ID)).thenReturn(기존_일정);
            doThrow(new EntityNotFoundException(ErrorCode.STATUS_NOT_FOUND))
                    .when(statusRepository).findByIdOrThrow(잘못된_상태_ID);

            // when & then
            assertThatThrownBy(() -> taskService.editTask(기존_일정_ID, 일정_변경_요청))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("존재하지 않는 프로젝트 상태입니다.");
        }
    }

    @Nested
    class 사용자가_프로젝트_일정_삭제시에 {

        @Test
        void 성공한다() {
            // given
            Long 기존_일정_ID = 1L;
            Task 기존_일정 = 일정은().식별자는(기존_일정_ID).이다();

            when(taskRepository.findByIdOrThrow(기존_일정_ID)).thenReturn(기존_일정);
            doNothing().when(taskRepository).delete(기존_일정);

            // when
            taskService.deleteTask(기존_일정_ID);

            // then
            verify(taskRepository).delete(기존_일정);
        }

        @Test
        void 일정이_없으면_예외가_발생한다() {
            // given
            Long 기존_일정_ID = 1L;

            doThrow(new EntityNotFoundException(ErrorCode.TASK_NOT_FOUND))
                    .when(taskRepository).findByIdOrThrow(기존_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskService.deleteTask(기존_일정_ID))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("존재하지 않는 프로젝트 일정입니다.");
        }
    }
}
