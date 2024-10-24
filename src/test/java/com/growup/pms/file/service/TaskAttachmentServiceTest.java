package com.growup.pms.file.service;

import static com.growup.pms.test.fixture.task.builder.TaskAttachmentTestBuilder.일정_첨부파일은;
import static com.growup.pms.test.fixture.task.builder.TaskTestBuilder.일정은;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.domain.TaskAttachment;
import com.growup.pms.task.repository.TaskAttachmentRepository;
import com.growup.pms.task.repository.TaskRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TaskAttachmentServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskAttachmentRepository taskAttachmentRepository;

    @Mock
    FileStorageService fileStorageService;

    @InjectMocks
    TaskAttachmentService taskAttachmentService;

    @Nested
    class 일정_첨부파일_업로드시 {

        @Test
        void 성공한다() {
            // given
            Long 일정_ID = 1L;
            Task 기존_일정 = 일정은().이다();

            byte[] 파일_내용 = "GU-99".getBytes();
            MockMultipartFile 첨부_파일 = new MockMultipartFile("test.img", 파일_내용);

            when(taskRepository.findByIdOrThrow(일정_ID)).thenReturn(기존_일정);

            // when & then
            assertThatCode(() -> taskAttachmentService.upload(일정_ID, 첨부_파일))
                    .doesNotThrowAnyException();
        }

        @Test
        void 일정이_없으면_예외가_발생한다() {
            // given
            Long 잘못된_일정_ID = Long.MIN_VALUE;

            byte[] 파일_내용 = "GU-99".getBytes();
            MockMultipartFile 첨부_파일 = new MockMultipartFile("test.img", 파일_내용);

            doThrow(BusinessException.class).when(taskRepository).findByIdOrThrow(잘못된_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskAttachmentService.upload(잘못된_일정_ID, 첨부_파일))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class 일정_첨부파일_다운로드시 {

        @Test
        void 성공한다() {
            // given
            Long 일정_ID = 1L;
            Task 기존_일정 = 일정은().이다();

            String 파일_이름 = "test.png";
            byte[] 파일_내용 = "test".getBytes();

            when(taskRepository.findByIdOrThrow(일정_ID)).thenReturn(기존_일정);
            when(fileStorageService.download(anyString())).thenReturn(파일_내용);

            // when
            byte[] 실제_결과 = taskAttachmentService.download(일정_ID, 파일_이름);

            // then
            assertThat(실제_결과).isEqualTo(파일_내용);
        }

        @Test
        void 일정이_없으면_예외가_발생한다() {
            // given
            Long 잘못된_일정_ID = Long.MIN_VALUE;
            String 파일_이름 = "test.png";

            doThrow(BusinessException.class).when(taskRepository).findByIdOrThrow(잘못된_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskAttachmentService.download(잘못된_일정_ID, 파일_이름))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class 일정_첨부파일_삭제시 {

        @Test
        void 성공한다() {
            // given
            Long 일정_ID = 1L;
            Task 기존_일정 = 일정은().이다();

            Long 첨부파일_ID = 1L;
            TaskAttachment 기존_첨부파일 = 일정_첨부파일은().이다();
            when(taskRepository.findByIdOrThrow(일정_ID)).thenReturn(기존_일정);
            when(taskAttachmentRepository.findByIdOrThrow(첨부파일_ID)).thenReturn(기존_첨부파일);

            // then
            taskAttachmentService.deleteTaskAttachment(일정_ID, 첨부파일_ID);

            // then
            verify(taskAttachmentRepository).delete(기존_첨부파일);
        }

        @Test
        void 일정이_없으면_예외가_발생한다() {
            // given
            Long 잘못된_일정_ID = Long.MIN_VALUE;
            Long 첨부파일_ID = 1L;
            doThrow(new BusinessException(ErrorCode.TASK_NOT_FOUND))
                    .when(taskRepository).findByIdOrThrow(잘못된_일정_ID);

            // when & then
            assertThatThrownBy(() -> taskAttachmentService.deleteTaskAttachment(잘못된_일정_ID, 첨부파일_ID))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TASK_NOT_FOUND);
        }

        @Test
        void 첨부파일이_없으면_예외가_발생한다() {
            // given
            Long 기존_일정_ID = 1L;
            Task 기존_일정 = 일정은().이다();
            Long 잘못된_첨부파일_ID = Long.MIN_VALUE;
            when(taskRepository.findByIdOrThrow(기존_일정_ID)).thenReturn(기존_일정);
            doThrow(new BusinessException(ErrorCode.FILE_NOT_FOUND))
                    .when(taskAttachmentRepository).findByIdOrThrow(잘못된_첨부파일_ID);

            // when & then
            assertThatThrownBy(() -> taskAttachmentService.deleteTaskAttachment(기존_일정_ID, 잘못된_첨부파일_ID))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FILE_NOT_FOUND);
        }
    }
}
