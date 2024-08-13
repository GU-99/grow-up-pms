package com.growup.pms.status.service;

import static com.growup.pms.test.fixture.project.ProjectTestBuilder.프로젝트는;
import static com.growup.pms.test.fixture.status.StatusCreateRequestTestBuilder.상태_생성_요청은;
import static com.growup.pms.test.fixture.status.StatusResponseTestBuilder.상태_응답은;
import static com.growup.pms.test.fixture.status.StatusTestBuilder.상태는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.project.domain.Project;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.status.controller.dto.response.StatusResponse;
import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.status.service.dto.StatusCreateCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import java.util.ArrayList;
import java.util.List;
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
class StatusServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    StatusRepository statusRepository;

    @InjectMocks
    StatusService statusService;

    @Nested
    class 사용자가_프로젝트_상태_생성시에 {

        @Test
        void 성공적으로_프로젝트_상태를_생성한다() {
            // given
            Long 예상_상태_ID = 1L;
            Long 프로젝트_ID = 1L;
            Project 예상_프로젝트 = 프로젝트는().식별자가(프로젝트_ID).이다();
            Status 생성된_상태 = 상태는().식별자가(예상_상태_ID).프로젝트가(예상_프로젝트).이다();
            StatusCreateCommand 상태_생성_요청 = 상태_생성_요청은().이다().toCommand(프로젝트_ID);

            when(projectRepository.findById(프로젝트_ID)).thenReturn(Optional.of(예상_프로젝트));
            when(statusRepository.save(any(Status.class))).thenReturn(생성된_상태);

            // when
            StatusResponse 실제_상태_생성_응답 = statusService.createStatus(상태_생성_요청);

            // then
            assertThat(실제_상태_생성_응답.statusId()).isEqualTo(예상_상태_ID);
        }

        @Test
        void 프로젝트가_존재하지_않으면_예외가_발생한다() {
            // given
            Long 프로젝트_ID = 1L;
            StatusCreateCommand 상태_생성_요청 = 상태_생성_요청은().이다().toCommand(프로젝트_ID);

            // when & then
            assertThatThrownBy(() -> statusService.createStatus(상태_생성_요청))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class 사용자가_프로젝트_상태_전체조회시에 {

        @Test
        void 성공한다() {
            // given
            Long 예상_프로젝트_ID = 1L;
            StatusResponse 예상_응답1 = 상태_응답은().상태_식별자는(1L)
                    .프로젝트_식별자는(예상_프로젝트_ID)
                    .이름은("할일")
                    .색상코드는("345678")
                    .정렬순서는((short) 1)
                    .이다();

            StatusResponse 예상_응답2 = 상태_응답은().상태_식별자는(2L)
                    .프로젝트_식별자는(예상_프로젝트_ID)
                    .이름은("진행중")
                    .색상코드는("B4FF3B")
                    .정렬순서는((short) 2)
                    .이다();

            StatusResponse 예상_응답3 = 상태_응답은().상태_식별자는(3L)
                    .프로젝트_식별자는(예상_프로젝트_ID)
                    .이름은("완료")
                    .색상코드는("A03FFF")
                    .정렬순서는((short) 3)
                    .이다();

            List<StatusResponse> 예상_결과 = List.of(예상_응답1, 예상_응답2, 예상_응답3);
            when(statusRepository.getAllStatusByProjectId(예상_프로젝트_ID))
                    .thenReturn(예상_결과);

            // when
            List<StatusResponse> 실제_결과 = statusService.getStatuses(예상_프로젝트_ID);

            // then
            assertThat(실제_결과).isEqualTo(예상_결과);
        }

        @Test
        void 프로젝트에_상태가_없다면_빈_리스트를_반환한다() {
            // given
            Long 예상_프로젝트_ID = 1L;
            List<StatusResponse> 예상_결과 = new ArrayList<>();

            when(statusRepository.getAllStatusByProjectId(예상_프로젝트_ID))
                    .thenReturn(예상_결과);

            // when
            List<StatusResponse> 실제_결과 = statusService.getStatuses(예상_프로젝트_ID);

            // then
            assertThat(실제_결과).isEqualTo(예상_결과);
        }
    }
}
