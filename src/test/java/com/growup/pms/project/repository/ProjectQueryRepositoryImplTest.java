package com.growup.pms.project.repository;

import static com.growup.pms.test.fixture.project.builder.ProjectTestBuilder.프로젝트는;
import static com.growup.pms.test.fixture.team.builder.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;

import com.growup.pms.project.controller.dto.response.ProjectResponse;
import com.growup.pms.project.domain.Project;
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
class ProjectQueryRepositoryImplTest extends RepositoryTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectQueryRepositoryImpl projectQueryRepository;

    User 브라운, 레니, 레너드;
    Team GU팀;
    Project PMS_프로젝트, 게시판_프로젝트, 팀원모집_프로젝트;

    @BeforeEach
    void setUp() {
        브라운 = userRepository.save(사용자는().식별자가(1L).아이디가("brown").닉네임이("브라운").이다());
        레니 = userRepository.save(사용자는().식별자가(2L).아이디가("lenny").닉네임이("레니").이다());
        레너드 = userRepository.save(사용자는().식별자가(3L).아이디가("leonard").닉네임이("레너드").이다());

        GU팀 = teamRepository.save(팀은().식별자가(1L).이름이("GU팀").팀장이(브라운).이다());

        PMS_프로젝트 = projectRepository.save(
                프로젝트는().식별자가(1L)
                        .이름이("PMS 프로젝트")
                        .설명이("프로젝트 관리 서비스를 개발하는 프로젝트")
                        .팀이(GU팀)
                        .시작일이(LocalDate.of(2024, 1, 1))
                        .종료일이(LocalDate.of(2024, 12, 31))
                        .이다()
        );

        게시판_프로젝트 = projectRepository.save(
                프로젝트는()
                        .식별자가(2L)
                        .이름이("게시판 프로젝트")
                        .설명이("게시판을 개발하는 프로젝트")
                        .팀이(GU팀)
                        .시작일이(LocalDate.of(2023, 1, 1))
                        .종료일이(LocalDate.of(2023, 12, 31))
                        .이다()
        );

        팀원모집_프로젝트 = projectRepository.save(
                프로젝트는()
                        .식별자가(3L)
                        .이름이("팀원모집 프로젝트")
                        .설명이("팀원모집 플랫폼을 개발하는 프로젝트")
                        .팀이(GU팀)
                        .시작일이(LocalDate.of(2022, 1, 1))
                        .종료일이(LocalDate.of(2022, 12, 31))
                        .이다()
        );
    }

    @Nested
    class 팀_프로젝트_목록_조회시 {

        @Test
        void 성공한다() {
            // given
            Long 팀_ID = GU팀.getId();

            // when
            List<ProjectResponse> 실제_결과 = projectQueryRepository.getProjectsByTeamId(팀_ID);

            // then
            assertThat(실제_결과).hasSize(3);
        }

        @Test
        void 프로젝트가_없으면_빈리스트를_반환한다() {
            // given
            Long 잘못된_팀_ID = Long.MIN_VALUE;

            // when
            List<ProjectResponse> 실제_결과 = projectQueryRepository.getProjectsByTeamId(잘못된_팀_ID);

            // then
            assertThat(실제_결과).isEmpty();
        }
    }
}
