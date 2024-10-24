package com.growup.pms.task.service;

import static com.growup.pms.test.fixture.status.builder.StatusTestBuilder.상태는;
import static com.growup.pms.test.fixture.task.builder.TaskAttachmentResponseTestBuilder.첨부파일_조회_응답은;
import static com.growup.pms.test.fixture.task.builder.TaskCreateRequestTestBuilder.일정_생성_요청은;
import static com.growup.pms.test.fixture.task.builder.TaskDetailResponseTestBuilder.일정_상세조회_응답은;
import static com.growup.pms.test.fixture.task.builder.TaskEditRequestTestBuilder.일정_수정_요청은;
import static com.growup.pms.test.fixture.task.builder.TaskKanbanResponseTestBuilder.일정_칸반_응답은;
import static com.growup.pms.test.fixture.task.builder.TaskOrderEditRequestTestBuilder.일정_순서변경_요청은;
import static com.growup.pms.test.fixture.task.builder.TaskResponseTestBuilder.일정_전체조회_응답은;
import static com.growup.pms.test.fixture.task.builder.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.task.controller.dto.response.TaskAttachmentResponse;
import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.controller.dto.response.TaskKanbanResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.repository.TaskAttachmentRepository;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.task.repository.TaskUserRepository;
import com.growup.pms.task.service.dto.TaskCreateCommand;
import com.growup.pms.task.service.dto.TaskEditCommand;
import com.growup.pms.task.service.dto.TaskOrderEditCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @Mock
    TaskUserRepository taskUserRepository;

    @Mock
    TaskAttachmentRepository taskAttachmentRepository;

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
            List<Long> 예상_담당자_ID_목록 = List.of(1L, 2L, 3L);
            Status 예상_상태 = 상태는().식별자가(예상_상태_ID).이다();
            List<User> 예상_담당자_목록 = List.of(사용자는().식별자가(1L).이다(), 사용자는().식별자가(2L).이다(), 사용자는().식별자가(3L).이다());
            Task 예상_일정 = 일정은().이다();
            TaskCreateCommand 예상_일정_생성_요청 = 일정_생성_요청은().이다().toCommand();

            when(statusRepository.findByIdOrThrow(예상_상태_ID)).thenReturn(예상_상태);
            when(userRepository.findAllById(예상_담당자_ID_목록)).thenReturn(예상_담당자_목록);
            when(taskRepository.save(any(Task.class))).thenReturn(예상_일정);

            // when
            TaskDetailResponse 실제_결과 = taskService.createTask(예상_프로젝트_ID, 예상_일정_생성_요청);

            // then
            assertThat(실제_결과.getTaskId()).isEqualTo(예상_일정_ID);
        }

        @Test
        void 담당자가_없어도_성공한다() {
            Long 예상_일정_ID = 1L;
            Long 예상_프로젝트_ID = 1L;
            Long 예상_상태_ID = 1L;
            List<Long> 예상_담당자_ID_목록 = Collections.emptyList();
            Status 예상_상태 = 상태는().식별자가(예상_상태_ID).이다();
            List<User> 예상_담당자_목록 = Collections.emptyList();
            Task 예상_일정 = 일정은().이다();
            TaskCreateCommand 예상_일정_생성_요청 = 일정_생성_요청은().담당자_ID_목록은(예상_담당자_ID_목록).이다().toCommand();

            when(statusRepository.findByIdOrThrow(예상_상태_ID)).thenReturn(예상_상태);
            when(userRepository.findAllById(예상_담당자_ID_목록)).thenReturn(예상_담당자_목록);
            when(taskRepository.save(any(Task.class))).thenReturn(예상_일정);

            // when
            TaskDetailResponse 실제_결과 = taskService.createTask(예상_프로젝트_ID, 예상_일정_생성_요청);

            // then
            assertThat(실제_결과.getTaskId()).isEqualTo(예상_일정_ID);
            assertThat(실제_결과.getStatusId()).isEqualTo(예상_상태_ID);
        }

        @Test
        void 상태가_없으면_예외가_발생한다() {
            // given
            Long 예상_프로젝트_ID = 1L;
            Long 예상_상태_ID = 1L;
            TaskCreateCommand 예상_일정_생성_요청 = 일정_생성_요청은().이다().toCommand();

            doThrow(new BusinessException(ErrorCode.STATUS_NOT_FOUND))
                    .when(statusRepository).findByIdOrThrow(예상_상태_ID);

            // when &then
            assertThatThrownBy(() -> taskService.createTask(예상_프로젝트_ID, 예상_일정_생성_요청))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 프로젝트 상태를 찾을 수 없습니다. 유효한 상태를 선택해 주세요.");
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

            Status 예상_상태_1 = 상태는().식별자가(예상_상태_ID_1).이다();
            Status 예상_상태_2 = 상태는().식별자가(예상_상태_ID_2).이다();

            TaskResponse 예상_일정_1 = 일정_전체조회_응답은()
                    .일정_식별자는(1L)
                    .상태_식별자는(예상_상태_ID_1)
                    .일정이름은("PMS 프로젝트의 환경설정을 진행함")
                    .정렬순서는((short) 1)
                    .이다();

            TaskResponse 예상_일정_2 = 일정_전체조회_응답은()
                    .일정_식별자는(2L)
                    .상태_식별자는(예상_상태_ID_1)
                    .일정이름은("PMS 프로젝트의 등록 기능 구현을 진행함")
                    .정렬순서는((short) 2)
                    .이다();

            TaskResponse 예상_일정_3 = 일정_전체조회_응답은()
                    .일정_식별자는(3L)
                    .상태_식별자는(예상_상태_ID_2)
                    .일정이름은("PMS 프로젝트의 조회 기능 구현을 진행함")
                    .정렬순서는((short) 3)
                    .이다();

            List<TaskResponse> 예상_일정_목록_2 = List.of(예상_일정_3);
            List<TaskResponse> 예상_일정_목록_1 = List.of(예상_일정_1, 예상_일정_2);
            Map<Long, List<TaskResponse>> 예상_상태별_일정 = Map.of(예상_상태_ID_1, 예상_일정_목록_1, 예상_상태_ID_2, 예상_일정_목록_2);

            TaskKanbanResponse 예상_응답_1 = 일정_칸반_응답은().일정목록은(예상_일정_목록_1).이다();
            TaskKanbanResponse 예상_응답_2 = 일정_칸반_응답은().상태_식별자는(예상_상태_ID_2).일정목록은(예상_일정_목록_2).이다();

            List<TaskKanbanResponse> 예상_결과 = List.of(예상_응답_1, 예상_응답_2);

            when(taskRepository.getTasksByProjectId(예상_프로젝트_ID)).thenReturn(예상_상태별_일정);
            when(statusRepository.findByIdOrThrow(예상_상태_ID_1)).thenReturn(예상_상태_1);
            when(statusRepository.findByIdOrThrow(예상_상태_ID_2)).thenReturn(예상_상태_2);

            // when
            List<TaskKanbanResponse> 실제_결과 = taskService.getTasks(예상_프로젝트_ID);

            // then
            assertThat(실제_결과).hasSize(2);
            assertThat(실제_결과.size()).isEqualTo(예상_결과.size());
        }

        @Test
        void 해당_프로젝트에_일정이_없으면_빈맵을_반환한다() {
            // given
            Long 잘못된_프로젝트_ID = Long.MAX_VALUE;

            Map<Long, List<TaskResponse>> 예상_결과 = Collections.emptyMap();
            when(taskRepository.getTasksByProjectId(잘못된_프로젝트_ID)).thenReturn(예상_결과);

            // when
            List<TaskKanbanResponse> 실제_결과 = taskService.getTasks(잘못된_프로젝트_ID);

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

            doThrow(new BusinessException(ErrorCode.TASK_NOT_FOUND))
                    .when(taskRepository).findByIdOrThrow(존재하지_않는_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskService.getTask(존재하지_않는_일정_ID))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TASK_NOT_FOUND);
        }
    }

    @Nested
    class 사용자가_프로젝트_일정_변경시에 {

        @Test
        void 성공한다() {
            // given
            Long 기존_일정_ID = 1L;
            Task 기존_일정 = 일정은().식별자는(기존_일정_ID).이다();
            Long 변경할_상태_ID = 2L;
            Status 변경할_상태 = 상태는().식별자가(변경할_상태_ID).이다();
            String 변경할_일정_이름 = "변경할 이름 입니다!";
            String 변경할_내용 = "변경할 내용!!!!!!!!!!#@#%^#$^&*%(^*&(^%$#231382304-2315982ㅅ89asdfjlaiejvlsakc";
            LocalDate 변경할_시작일자 = LocalDate.of(2023, 1, 1);
            LocalDate 변경할_종료일자 = LocalDate.of(2023, 3, 1);
            TaskEditCommand 일정_변경_요청 = 일정_수정_요청은()
                    .상태_식별자는(변경할_상태_ID)
                    .일정이름은(변경할_일정_이름)
                    .본문내용은(변경할_내용)
                    .시작일자는(변경할_시작일자)
                    .종료일자는(변경할_종료일자)
                    .이다().toCommand();

            when(taskRepository.findByIdOrThrow(기존_일정_ID)).thenReturn(기존_일정);
            when(statusRepository.findByIdOrThrow(변경할_상태_ID)).thenReturn(변경할_상태);

            // when
            taskService.editTask(기존_일정_ID, 일정_변경_요청);

            // then
            assertSoftly(softly -> {
                softly.assertThat(기존_일정.getStatus().getId()).isEqualTo(변경할_상태_ID);
                softly.assertThat(기존_일정.getName()).isEqualTo(변경할_일정_이름);
                softly.assertThat(기존_일정.getContent()).isEqualTo(변경할_내용);
                softly.assertThat(기존_일정.getStartDate()).isEqualTo(변경할_시작일자);
                softly.assertThat(기존_일정.getEndDate()).isEqualTo(변경할_종료일자);
            });
        }

        @Test
        void 일정이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 잘못된_일정_ID = 1L;
            TaskEditCommand 일정_변경_요청 = 일정_수정_요청은().이다().toCommand();

            doThrow(new BusinessException(ErrorCode.TASK_NOT_FOUND))
                    .when(taskRepository).findByIdOrThrow(잘못된_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskService.editTask(잘못된_일정_ID, 일정_변경_요청))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TASK_NOT_FOUND);
        }

        @Test
        void 변경하려는_상태가_존재하지_않으면_예외가_발생한다() {
            // given
            Long 기존_일정_ID = 1L;
            Task 기존_일정 = 일정은().식별자는(기존_일정_ID).이다();
            Long 잘못된_상태_ID = 2L;
            TaskEditCommand 일정_변경_요청 = 일정_수정_요청은().상태_식별자는(잘못된_상태_ID).이다().toCommand();

            when(taskRepository.findByIdOrThrow(기존_일정_ID)).thenReturn(기존_일정);
            doThrow(new BusinessException(ErrorCode.STATUS_NOT_FOUND))
                    .when(statusRepository).findByIdOrThrow(잘못된_상태_ID);

            // when & then
            assertThatThrownBy(() -> taskService.editTask(기존_일정_ID, 일정_변경_요청))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.STATUS_NOT_FOUND);
        }
    }

    @Nested
    class 사용자가_프로젝트_일정_순서_변경시에 {

        @Test
        void 성공한다() {
            // given
            Long 기존_일정_ID_1 = 1L;
            Long 기존_일정_ID_2 = 2L;
            Task 기존_일정_1 = 일정은().식별자는(기존_일정_ID_1).이다();
            Task 기존_일정_2 = 일정은().식별자는(기존_일정_ID_2).정렬순서는((short) 2).이다();
            Long 변경할_상태_ID_1 = 2L;
            Long 변경할_상태_ID_2 = 3L;
            Status 변경할_상태_1 = 상태는().식별자가(변경할_상태_ID_1).이름이("진행중").이다();
            Status 변경할_상태_2 = 상태는().식별자가(변경할_상태_ID_2).이름이("완료").이다();

            TaskOrderEditCommand 상태_순서변경_요청_1 = 일정_순서변경_요청은()
                    .상태_식별자는(변경할_상태_ID_1)
                    .정렬순서는((short) 4)
                    .이다().toCommand();
            TaskOrderEditCommand 상태_순서변경_요청_2 = 일정_순서변경_요청은()
                    .일정_식별자는(기존_일정_ID_2)
                    .상태_식별자는(변경할_상태_ID_2)
                    .정렬순서는((short) 5)
                    .이다().toCommand();
            List<TaskOrderEditCommand> 상태_순서변경_요청_리스트 = List.of(상태_순서변경_요청_1, 상태_순서변경_요청_2);

            when(statusRepository.findByIdOrThrow(변경할_상태_ID_1)).thenReturn(변경할_상태_1);
            when(statusRepository.findByIdOrThrow(변경할_상태_ID_2)).thenReturn(변경할_상태_2);
            when(taskRepository.findByIdOrThrow(기존_일정_ID_1)).thenReturn(기존_일정_1);
            when(taskRepository.findByIdOrThrow(기존_일정_ID_2)).thenReturn(기존_일정_2);

            // when
            taskService.editTaskOrder(상태_순서변경_요청_리스트);

            // then
            assertSoftly(softly -> {
                softly.assertThat(기존_일정_1.getSortOrder()).isEqualTo(상태_순서변경_요청_1.sortOrder());
                softly.assertThat(기존_일정_2.getSortOrder()).isEqualTo(상태_순서변경_요청_2.sortOrder());
                softly.assertThat(기존_일정_1.getStatus().getId()).isEqualTo(변경할_상태_ID_1);
                softly.assertThat(기존_일정_2.getStatus().getId()).isEqualTo(변경할_상태_ID_2);
            });
        }

        @Test
        void 일정이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 잘못된_일정_ID = Long.MAX_VALUE;
            Long 기존_일정_ID = 1L;
            Task 기존_일정 = 일정은().식별자는(기존_일정_ID).이다();
            Long 변경할_상태_ID_1 = 2L;
            Long 변경할_상태_ID_2 = 3L;
            Status 변경할_상태_1 = 상태는().식별자가(변경할_상태_ID_1).이름이("진행중").이다();

            TaskOrderEditCommand 상태_순서변경_요청_1 = 일정_순서변경_요청은()
                    .상태_식별자는(변경할_상태_ID_1)
                    .정렬순서는((short) 4)
                    .이다().toCommand();
            TaskOrderEditCommand 상태_순서변경_요청_2 = 일정_순서변경_요청은()
                    .일정_식별자는(잘못된_일정_ID)
                    .상태_식별자는(변경할_상태_ID_2)
                    .정렬순서는((short) 5)
                    .이다().toCommand();
            List<TaskOrderEditCommand> 상태_순서변경_요청_리스트 = List.of(상태_순서변경_요청_1, 상태_순서변경_요청_2);

            when(statusRepository.findByIdOrThrow(변경할_상태_ID_1)).thenReturn(변경할_상태_1);
            when(taskRepository.findByIdOrThrow(기존_일정_ID)).thenReturn(기존_일정);
            doThrow(new BusinessException(ErrorCode.TASK_NOT_FOUND))
                    .when(taskRepository).findByIdOrThrow(잘못된_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskService.editTaskOrder(상태_순서변경_요청_리스트))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TASK_NOT_FOUND);
        }

        @Test
        void 변경하려는_상태가_존재하지_않으면_예외가_발생한다() {
            // given
            Long 기존_일정_ID = 2L;
            Long 잘못된_상태_ID = Long.MAX_VALUE;
            Long 변경할_상태_ID = 3L;

            TaskOrderEditCommand 상태_순서변경_요청_1 = 일정_순서변경_요청은()
                    .상태_식별자는(잘못된_상태_ID)
                    .정렬순서는((short) 4)
                    .이다().toCommand();
            TaskOrderEditCommand 상태_순서변경_요청_2 = 일정_순서변경_요청은()
                    .일정_식별자는(기존_일정_ID)
                    .상태_식별자는(변경할_상태_ID)
                    .정렬순서는((short) 5)
                    .이다().toCommand();
            List<TaskOrderEditCommand> 상태_순서변경_요청_리스트 = List.of(상태_순서변경_요청_1, 상태_순서변경_요청_2);

            doThrow(new BusinessException(ErrorCode.STATUS_NOT_FOUND))
                    .when(statusRepository).findByIdOrThrow(잘못된_상태_ID);

            // when & then
            assertThatThrownBy(() -> taskService.editTaskOrder(상태_순서변경_요청_리스트))
                    .isInstanceOf(BusinessException.class);

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

            doThrow(new BusinessException(ErrorCode.TASK_NOT_FOUND))
                    .when(taskRepository).findByIdOrThrow(기존_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskService.deleteTask(기존_일정_ID))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TASK_NOT_FOUND);
        }
    }

    @Nested
    class 사용자가_프로젝트_일정_첨부파일_조회시에 {

        @Test
        void 성공한다() {
            // given
            Long 일정_ID = 1L;
            TaskAttachmentResponse 예상_응답_1 = 첨부파일_조회_응답은().이다();
            TaskAttachmentResponse 예상_응답_2 = 첨부파일_조회_응답은().첨부파일_식별자가(2L).원본_파일명이("cow.docx")
                    .저장_파일명이("cow-store-name.docx").이다();
            TaskAttachmentResponse 예상_응답_3 = 첨부파일_조회_응답은().첨부파일_식별자가(3L).원본_파일명이("cat.pdf")
                    .저장_파일명이("cat-store-name.pdf").이다();
            List<TaskAttachmentResponse> 예상_응답_목록 = List.of(예상_응답_1, 예상_응답_2, 예상_응답_3);

            when(taskAttachmentRepository.getTaskAttachmentsByTaskId(anyLong())).thenReturn(예상_응답_목록);

            // when
            List<TaskAttachmentResponse> 실제_결과 = taskService.getTaskAttachments(일정_ID);

            // then
            assertSoftly(softly -> {
                softly.assertThat(실제_결과).hasSize(예상_응답_목록.size())
                        .extracting("fileId", "fileName", "uploadName")
                        .containsExactlyInAnyOrder(
                                tuple(예상_응답_1.fileId(), 예상_응답_1.fileName(), 예상_응답_1.uploadName()),
                                tuple(예상_응답_2.fileId(), 예상_응답_2.fileName(), 예상_응답_2.uploadName()),
                                tuple(예상_응답_3.fileId(), 예상_응답_3.fileName(), 예상_응답_3.uploadName())
                        );
            });
        }

        @Test
        void 일정이_없거나_첨부파일이_없으면_빈리스트를_반환한다() {
            // given
            Long 잘못된_일정_ID = 1L;
            List<TaskAttachmentResponse> 예상_결과 = Collections.emptyList();

            when(taskAttachmentRepository.getTaskAttachmentsByTaskId(anyLong())).thenReturn(예상_결과);

            // when
            List<TaskAttachmentResponse> 실제_결과 = taskService.getTaskAttachments(잘못된_일정_ID);

            // then
            assertThat(실제_결과).isEmpty();
        }
    }
}
