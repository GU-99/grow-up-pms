package com.growup.pms.project.repository;

import static com.growup.pms.test.fixture.project.builder.ProjectTestBuilder.프로젝트는;
import static com.growup.pms.test.fixture.project.builder.ProjectUserTestBuilder.프로젝트_유저는;
import static com.growup.pms.test.fixture.role.builder.RoleTestBuilder.역할은;
import static com.growup.pms.test.fixture.team.builder.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;

import com.growup.pms.project.controller.dto.response.ProjectUserResponse;
import com.growup.pms.project.domain.Project;
import com.growup.pms.project.domain.ProjectUser;
import com.growup.pms.role.domain.ProjectRole;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.RoleType;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.RepositoryTestSupport;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
public class ProjectUserQueryRepositoryTest extends RepositoryTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProjectUserRepository projectUserRepository;

    @Autowired
    ProjectUserQueryRepositoryImpl projectUserQueryRepository;

    User 브라운, 코니, 레니, 레너드;
    Team GU팀;
    Project PMS_프로젝트, 게시판_프로젝트, 팀원모집_프로젝트;
    ProjectUser PMS_ADMIN, PMS_LEADER, PMS_ASSIGNEE_1, PMS_ASSIGNEE_2;
    ProjectUser 게시판_ADMIN, 게시판_LEADER_1, 게시판_LEADER_2, 게시판_ASSIGNEE;
    ProjectUser 팀원모집_ADMIN, 팀원모집_LEADER;
    Role ADMIN, LEADER, ASSIGNEE;

    // 브라운: PMS, 게시판 ADMIN, 팀원모집 LEADER
    // 코니: PMS, 게시판 LEADER
    // 레니: PMS ASSIGNEE, 게시판 LEADER
    // 레너드: PMS ASSIGNEE, 게시판 ASSIGNEE, 팀원모집 ADMIN
    @BeforeEach
    void setUp() {
        브라운 = userRepository.save(사용자는().식별자가(1L).아이디가("brown").닉네임이("브라운").이다());
        코니 = userRepository.save(사용자는().식별자가(2L).아이디가("conny").닉네임이("코니").이다());
        레니 = userRepository.save(사용자는().식별자가(3L).아이디가("lenny").닉네임이("레니").이다());
        레너드 = userRepository.save(사용자는().식별자가(4L).아이디가("leonard").닉네임이("레너드").이다());

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

        ADMIN = roleRepository.save(
                역할은()
                        .식별자가(1L)
                        .타입이(RoleType.PROJECT)
                        .이름이(ProjectRole.ADMIN.getRoleName())
                        .이다()
        );

        LEADER = roleRepository.save(
                역할은()
                        .식별자가(2L)
                        .타입이(RoleType.PROJECT)
                        .이름이(ProjectRole.LEADER.getRoleName())
                        .이다()
        );

        ASSIGNEE = roleRepository.save(
                역할은()
                        .식별자가(3L)
                        .타입이(RoleType.PROJECT)
                        .이름이(ProjectRole.ASSIGNEE.getRoleName())
                        .이다()
        );

        PMS_ADMIN = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(브라운)
                        .권한이(ADMIN)
                        .프로젝트가(PMS_프로젝트)
                        .이다()
        );

        PMS_LEADER = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(코니)
                        .권한이(LEADER)
                        .프로젝트가(PMS_프로젝트)
                        .이다()
        );

        PMS_ASSIGNEE_1 = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(레니)
                        .권한이(ASSIGNEE)
                        .프로젝트가(PMS_프로젝트)
                        .이다()
        );

        PMS_ASSIGNEE_2 = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(레너드)
                        .권한이(ASSIGNEE)
                        .프로젝트가(PMS_프로젝트)
                        .이다()
        );

        게시판_ADMIN = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(브라운)
                        .권한이(ADMIN)
                        .프로젝트가(게시판_프로젝트)
                        .이다()
        );

        게시판_LEADER_1 = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(코니)
                        .권한이(LEADER)
                        .프로젝트가(게시판_프로젝트)
                        .이다()
        );

        게시판_LEADER_2 = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(레니)
                        .권한이(LEADER)
                        .프로젝트가(게시판_프로젝트)
                        .이다()
        );

        게시판_ASSIGNEE = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(레너드)
                        .권한이(ASSIGNEE)
                        .프로젝트가(게시판_프로젝트)
                        .이다()
        );

        팀원모집_ADMIN = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(레너드)
                        .권한이(ADMIN)
                        .프로젝트가(팀원모집_프로젝트)
                        .이다()
        );

        팀원모집_LEADER = projectUserRepository.save(
                프로젝트_유저는()
                        .회원이(브라운)
                        .권한이(LEADER)
                        .프로젝트가(팀원모집_프로젝트)
                        .이다()
        );
    }

    @Nested
    class 팀_프로젝트원_목록_조회시 {

        @Test
        void 성공한다() {
            // given
            Long PMS_ID = PMS_프로젝트.getId();
            Long 게시판_ID = 게시판_프로젝트.getId();
            Long 팀원모집_ID = 팀원모집_프로젝트.getId();

            // when
            List<ProjectUserResponse> PMS_실제_결과 = projectUserQueryRepository.getProjectUsersByProjectId(PMS_ID);
            List<ProjectUserResponse> 게시판_실제_결과 = projectUserQueryRepository.getProjectUsersByProjectId(게시판_ID);
            List<ProjectUserResponse> 팀원모집_실제_결과 = projectUserQueryRepository.getProjectUsersByProjectId(팀원모집_ID);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(PMS_실제_결과).hasSize(4);
                softly.assertThat(게시판_실제_결과).hasSize(4);
                softly.assertThat(팀원모집_실제_결과).hasSize(2);
            });
        }

        @Test
        void 해당_프로젝트가_없으면_빈리스트를_반환한다() {
            // given
            Long 잘못된_프로젝트_ID = Long.MIN_VALUE;

            // when
            List<ProjectUserResponse> 실졔_결과 = projectUserQueryRepository.getProjectUsersByProjectId(잘못된_프로젝트_ID);

            // then
            assertThat(실졔_결과).isEmpty();
        }
    }
}
