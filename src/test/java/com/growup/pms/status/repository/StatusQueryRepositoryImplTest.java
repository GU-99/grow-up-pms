package com.growup.pms.status.repository;


import static com.growup.pms.test.fixture.project.builder.ProjectTestBuilder.프로젝트는;
import static com.growup.pms.test.fixture.status.builder.StatusTestBuilder.상태는;
import static com.growup.pms.test.fixture.team.builder.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;

import com.growup.pms.project.domain.Project;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.status.controller.dto.response.StatusResponse;
import com.growup.pms.status.domain.Status;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.RepositoryTestSupport;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class StatusQueryRepositoryImplTest extends RepositoryTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    StatusQueryRepositoryImpl statusQueryRepository;

    User 브라운, 레니, 레너드;
    Team GU팀, 게시판팀;
    Project PMS_프로젝트, 게시판_프로젝트;
    Status PMS_할일, PMS_진행중, PMS_완료;

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
    }

    @Nested
    class 전체_상태_검색시 {

        @Test
        void 성공한다() {
            // given
            Long 프로젝트_ID = PMS_프로젝트.getId();

            // when
            List<StatusResponse> 실제_결과 = statusQueryRepository.getAllStatusByProjectId(프로젝트_ID);

            // then
            assertThat(실제_결과).hasSize(3);
            assertThat(실제_결과.stream().map(StatusResponse::name))
                    .containsExactlyInAnyOrder("할일", "진행중", "완료");
        }

        @Test
        void 프로젝트에_상태가_없으면_빈_리스트를_반환한다() {
            // given
            Long 프로젝트_ID = 게시판_프로젝트.getId();

            // when
            List<StatusResponse> 실졔_결과 = statusQueryRepository.getAllStatusByProjectId(프로젝트_ID);

            // then
            assertThat(실졔_결과).isEmpty();
        }
    }
}
