package com.growup.pms.task.repository;

import static com.growup.pms.test.fixture.project.ProjectTestBuilder.프로젝트는;
import static com.growup.pms.test.fixture.status.StatusTestBuilder.상태는;
import static com.growup.pms.test.fixture.task.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.team.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;

import com.growup.pms.project.domain.Project;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.RepositoryTestSupport;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class TaskQueryRepositoryImplTest extends RepositoryTestSupport {

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
    TaskQueryRepositoryImpl taskQueryRepository;

    User 브라운, 레니, 레너드;
    Team GU팀, 게시판팀;
    Project PMS_프로젝트, 게시판_프로젝트;
    Status PMS_할일, PMS_진행중, PMS_완료, PMS_보류;
    Task PMS_환경설정, PMS_등록기능, PMS_조회기능, PMS_수정기능, PMS_삭제기능;

    @BeforeEach
    void setUp() {
        브라운 = userRepository.save(사용자는().식별자가(1L).아이디가("brown").닉네임이("브라운").이다());
        레니 = userRepository.save(사용자는().식별자가(2L).아이디가("lenny").닉네임이("레니").이다());
        레너드 = userRepository.save(사용자는().식별자가(3L).아이디가("leonard").닉네임이("레너드").이다());

        GU팀 = teamRepository.save(팀은().식별자가(1L).이름이("GU팀").팀장이(브라운).이다());
        게시판팀 = teamRepository.save(팀은().식별자가(2L).이름이("게시판팀").팀장이(레니).이다());

        PMS_프로젝트 = projectRepository.save(
                프로젝트는().식별자가(1L)
                        .이름이("PMS 프로젝트")
                        .설명이("프로젝트 관리 서비스를 개발하는 프로젝트.  상태가 있습니다.")
                        .팀이(GU팀)
                        .시작일이(LocalDate.of(2024, 1, 1))
                        .종료일이(LocalDate.of(2024, 12, 31))
                        .이다()
        );

        게시판_프로젝트 = projectRepository.save(
                프로젝트는()
                        .식별자가(2L)
                        .이름이("게시판 프로젝트")
                        .설명이("게시판을 개발하는 프로젝트. 상태가 없습니다.")
                        .팀이(게시판팀)
                        .시작일이(LocalDate.of(2023, 1, 1))
                        .종료일이(LocalDate.of(2023, 12, 31))
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

        PMS_진행중 = statusRepository.save(
                상태는().식별자가(2L)
                        .프로젝트가(PMS_프로젝트)
                        .이름이("진행중")
                        .색상코드가("B4FF3B")
                        .정렬순서가((short) 2)
                        .이다()
        );

        PMS_완료 = statusRepository.save(
                상태는().식별자가(3L)
                        .프로젝트가(PMS_프로젝트)
                        .이름이("완료")
                        .색상코드가("A03FFF")
                        .정렬순서가((short) 3)
                        .이다()
        );

        PMS_보류 = statusRepository.save(
                상태는().식별자가(4L)
                        .프로젝트가(PMS_프로젝트)
                        .이름이("보류")
                        .색상코드가("A03001")
                        .정렬순서가((short) 3)
                        .이다()
        );

        PMS_환경설정 = taskRepository.save(
                일정은().식별자는(1L)
                        .상태는(PMS_완료)
                        .회원은(브라운)
                        .이름은("PMS 프로젝트의 환경설정을 진행함")
                        .내용은("- build.gradle 의존성 추가 <br> - Config 클래스 추가")
                        .정렬순서는((short) 1)
                        .시작일자는(LocalDate.parse("2023-01-01"))
                        .종료일자는(LocalDate.parse("2023-01-15"))
                        .이다()
        );
        PMS_등록기능 = taskRepository.save(
                일정은().식별자는(2L)
                        .상태는(PMS_완료)
                        .회원은(레니)
                        .이름은("PMS 프로젝트의 등록 기능 구현을 진행함")
                        .내용은("- ProjectRepository 구현 <br> - ProjectService 클래스 내부 구현")
                        .정렬순서는((short) 2)
                        .시작일자는(LocalDate.parse("2023-01-16"))
                        .종료일자는(LocalDate.parse("2023-01-31"))
                        .이다()
        );
        PMS_조회기능 = taskRepository.save(
                일정은().식별자는(3L)
                        .상태는(PMS_진행중)
                        .회원은(브라운)
                        .이름은("PMS 프로젝트의 조회 기능 구현을 진행함")
                        .내용은("- 조회 레포지토리 구현 <br> - 조회 쿼리 구현 및 테스트 작성")
                        .정렬순서는((short) 3)
                        .시작일자는(LocalDate.parse("2023-02-01"))
                        .종료일자는(null)
                        .이다()
        );
        PMS_수정기능 = taskRepository.save(
                일정은().식별자는(4L)
                        .상태는(PMS_할일)
                        .회원은(레너드)
                        .이름은("PMS 프로젝트의 수정 기능 구현을 진행함")
                        .내용은(null)
                        .정렬순서는((short) 4)
                        .시작일자는(null)
                        .종료일자는(null)
                        .이다()
        );
        PMS_삭제기능 = taskRepository.save(
                일정은().식별자는(5L)
                        .상태는(PMS_할일)
                        .회원은(null)
                        .이름은("PMS 프로젝트의 삭제 기능 구현을 진행함")
                        .내용은("- 누가누가 이 기능에 먼저 도착할까")
                        .정렬순서는((short) 5)
                        .시작일자는(null)
                        .종료일자는(null)
                        .이다()
        );
    }

    @Nested
    class 상태별_일정_전체_검색시 {

        @Test
        void 성공한다() {
            // given
            Long 프로젝트_ID = PMS_프로젝트.getId();
            Long 완료_상태_ID = PMS_완료.getId();
            Long 진행중_상태_ID = PMS_진행중.getId();
            Long 할일_상태_ID = PMS_할일.getId();
            Long 보류_상태_ID = PMS_보류.getId();

            // when
            Map<Long, List<TaskResponse>> 실제_결과 = taskQueryRepository.getTasksByProjectId(프로젝트_ID);
            for (Long l : 실제_결과.keySet()) {
                System.out.println(실제_결과.get(l));
            }

            // then
            assertThat(실제_결과.get(완료_상태_ID)).hasSize(2);
            assertThat(실제_결과.get(완료_상태_ID).stream().map(TaskResponse::taskName))
                    .containsExactly("PMS 프로젝트의 환경설정을 진행함", "PMS 프로젝트의 등록 기능 구현을 진행함");

            assertThat(실제_결과.get(진행중_상태_ID)).hasSize(1);
            assertThat(실제_결과.get(진행중_상태_ID).stream().map(TaskResponse::taskName))
                    .containsExactly("PMS 프로젝트의 조회 기능 구현을 진행함");

            assertThat(실제_결과.get(할일_상태_ID)).hasSize(2);
            assertThat(실제_결과.get(할일_상태_ID).stream().map(TaskResponse::taskName))
                    .containsExactly("PMS 프로젝트의 수정 기능 구현을 진행함", "PMS 프로젝트의 삭제 기능 구현을 진행함");

            assertThat(실제_결과.get(보류_상태_ID)).isNull();
        }

        @Test
        void 해당_프로젝트에_일정이_없으면_빈맵을_반환한다() {
            // given
            Long 잘못된_프로젝트_ID = Long.MAX_VALUE;

            // when
            Map<Long, List<TaskResponse>> 실제_결과 = taskQueryRepository.getTasksByProjectId(잘못된_프로젝트_ID);

            // then
            assertThat(실제_결과).isEmpty();
        }
    }
}
