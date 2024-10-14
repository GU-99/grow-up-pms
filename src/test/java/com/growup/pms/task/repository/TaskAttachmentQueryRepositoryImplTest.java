package com.growup.pms.task.repository;


import static com.growup.pms.test.fixture.project.builder.ProjectTestBuilder.프로젝트는;
import static com.growup.pms.test.fixture.status.builder.StatusTestBuilder.상태는;
import static com.growup.pms.test.fixture.task.builder.TaskAttachmentResponseTestBuilder.첨부파일_조회_응답은;
import static com.growup.pms.test.fixture.task.builder.TaskAttachmentTestBuilder.일정_첨부파일은;
import static com.growup.pms.test.fixture.task.builder.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.team.builder.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.groups.Tuple.tuple;

import com.growup.pms.project.domain.Project;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.task.controller.dto.response.TaskAttachmentResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.domain.TaskAttachment;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.RepositoryTestSupport;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class TaskAttachmentQueryRepositoryImplTest extends RepositoryTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskAttachmentRepository taskAttachmentRepository;

    @Autowired
    TaskAttachmentQueryRepositoryImpl taskAttachmentQueryRepository;

    User 브라운, 레니, 레너드;
    Team GU팀;
    Project PMS_프로젝트;
    Status PMS_할일;
    Task PMS_환경설정;
    TaskAttachment 첨부파일_돼지, 첨부파일_소, 첨부파일_고양이;

    @BeforeEach
    void setUp() {
        브라운 = userRepository.save(사용자는().식별자가(1L).아이디가("brown").닉네임이("브라운").이다());
        레니 = userRepository.save(사용자는().식별자가(2L).아이디가("lenny").닉네임이("레니").이다());
        레너드 = userRepository.save(사용자는().식별자가(3L).아이디가("leonard").닉네임이("레너드").이다());

        GU팀 = teamRepository.save(팀은().식별자가(1L).이름이("GU팀").팀장이(브라운).이다());

        PMS_프로젝트 = projectRepository.save(
                프로젝트는().식별자가(1L)
                        .이름이("PMS 프로젝트")
                        .설명이("프로젝트 관리 서비스를 개발하는 프로젝트.  상태가 있습니다.")
                        .팀이(GU팀)
                        .시작일이(LocalDate.of(2024, 1, 1))
                        .종료일이(LocalDate.of(2024, 12, 31))
                        .이다()
        );

        PMS_할일 = statusRepository.save(
                상태는().식별자가(1L)
                        .프로젝트가(PMS_프로젝트)
                        .이름이("할일")
                        .색상코드가("345678")
                        .정렬순서가((short) 1)
                        .이다()
        );

        PMS_환경설정 = taskRepository.save(
                일정은().식별자는(1L)
                        .상태는(PMS_할일)
                        .이름은("PMS 프로젝트의 환경설정을 진행함")
                        .내용은("- build.gradle 의존성 추가 <br> - Config 클래스 추가")
                        .정렬순서는((short) 2)
                        .시작일자는(LocalDate.parse("2023-01-01"))
                        .종료일자는(LocalDate.parse("2023-01-15"))
                        .이다()
        );

        첨부파일_돼지 = taskAttachmentRepository.save(
                일정_첨부파일은()
                        .첨부파일_식별자가(1L)
                        .원본파일명이("pig.jpg")
                        .저장파일명이("pig-store-name.jpg")
                        .이다()
        );

        첨부파일_소 = taskAttachmentRepository.save(
                일정_첨부파일은()
                        .첨부파일_식별자가(2L)
                        .원본파일명이("cow.docx")
                        .저장파일명이("cow-store-name.docx")
                        .이다()
        );

        첨부파일_고양이 = taskAttachmentRepository.save(
                일정_첨부파일은()
                        .첨부파일_식별자가(3L)
                        .원본파일명이("cat.pdf")
                        .저장파일명이("cat-store-name.pdf")
                        .이다()
        );
    }

    @Nested
    class 일정_첨부파일_목록_조회시 {

        @Test
        void 성공한다() {
            // given
            Long taskId = 1L;
            TaskAttachmentResponse 예상_응답_1 = 첨부파일_조회_응답은().첨부파일_식별자가(1L).원본_파일명이("pig.jpg")
                    .저장_파일명이("pig-store-name.jpg").이다();
            TaskAttachmentResponse 예상_응답_2 = 첨부파일_조회_응답은().첨부파일_식별자가(2L).원본_파일명이("cow.docx")
                    .저장_파일명이("cow-store-name.docx").이다();
            TaskAttachmentResponse 예상_응답_3 = 첨부파일_조회_응답은().첨부파일_식별자가(3L).원본_파일명이("cat.pdf")
                    .저장_파일명이("cat-store-name.pdf").이다();
            List<TaskAttachmentResponse> 예상_응답_목록 = List.of(예상_응답_1, 예상_응답_2, 예상_응답_3);

            // when
            List<TaskAttachmentResponse> 실제_결과 = taskAttachmentQueryRepository.getTaskAttachmentsByTaskId(taskId);

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
    }
}
