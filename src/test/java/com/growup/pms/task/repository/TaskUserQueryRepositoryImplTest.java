package com.growup.pms.task.repository;

import static com.growup.pms.test.fixture.project.builder.ProjectTestBuilder.프로젝트는;
import static com.growup.pms.test.fixture.project.builder.ProjectUserTestBuilder.프로젝트_유저는;
import static com.growup.pms.test.fixture.role.builder.RoleTestBuilder.역할은;
import static com.growup.pms.test.fixture.status.builder.StatusTestBuilder.상태는;
import static com.growup.pms.test.fixture.task.builder.TaskTestBuilder.일정은;
import static com.growup.pms.test.fixture.task.builder.TaskUserResponseTestBuilder.일정_수행자_목록_응답은;
import static com.growup.pms.test.fixture.task.builder.TaskUserTestBuilder.일정_수행자는;
import static com.growup.pms.test.fixture.team.builder.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.project.domain.Project;
import com.growup.pms.project.domain.ProjectUser;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.project.repository.ProjectUserRepository;
import com.growup.pms.role.domain.ProjectRole;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.RoleType;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.status.domain.Status;
import com.growup.pms.status.repository.StatusRepository;
import com.growup.pms.task.controller.dto.response.TaskUserResponse;
import com.growup.pms.task.domain.Task;
import com.growup.pms.task.domain.TaskUser;
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
class TaskUserQueryRepositoryImplTest extends RepositoryTestSupport {

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
    StatusRepository statusRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskUserRepository taskUserRepository;

    @Autowired
    TaskUserQueryRepositoryImpl taskUserQueryRepository;

    User 브라운, 코니, 레니, 레너드;
    Team GU팀;
    Project PMS_프로젝트;
    ProjectUser PMS_ADMIN, PMS_LEADER, PMS_ASSIGNEE_1, PMS_ASSIGNEE_2;
    Role ADMIN, LEADER, ASSIGNEE;
    Status PMS_할일;
    Task PMS_등록기능, PMS_조회기능, PMS_수정기능;
    TaskUser 등록기능_담당_1, 등록기능_담당_2, 조회기능_담당, 수정기능_담당;

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

        PMS_할일 = statusRepository.save(
                상태는().식별자가(1L)
                        .프로젝트가(PMS_프로젝트)
                        .이름이("할일")
                        .색상코드가("345678")
                        .정렬순서가((short) 1)
                        .이다()
        );

        PMS_등록기능 = taskRepository.save(
                일정은().식별자는(1L)
                        .상태는(PMS_할일)
                        .이름은("PMS 프로젝트의 등록 기능 구현을 진행함")
                        .내용은("- ProjectRepository 구현 <br> - ProjectService 클래스 내부 구현")
                        .정렬순서는((short) 1)
                        .시작일자는(LocalDate.parse("2023-01-16"))
                        .종료일자는(LocalDate.parse("2023-01-31"))
                        .이다()
        );

        PMS_조회기능 = taskRepository.save(
                일정은().식별자는(2L)
                        .상태는(PMS_할일)
                        .이름은("PMS 프로젝트의 조회 기능 구현을 진행함")
                        .내용은("- 조회 레포지토리 구현 <br> - 조회 쿼리 구현 및 테스트 작성")
                        .정렬순서는((short) 2)
                        .시작일자는(LocalDate.parse("2023-02-01"))
                        .종료일자는(null)
                        .이다()
        );

        PMS_수정기능 = taskRepository.save(
                일정은().식별자는(3L)
                        .상태는(PMS_할일)
                        .이름은("PMS 프로젝트의 수정 기능 구현을 진행함")
                        .내용은(null)
                        .정렬순서는((short) 4)
                        .시작일자는(null)
                        .종료일자는(null)
                        .이다()
        );

        등록기능_담당_1 = taskUserRepository.save(
                일정_수행자는()
                        .일정이(PMS_등록기능)
                        .사용자가(브라운)
                        .이다()
        );

        등록기능_담당_2 = taskUserRepository.save(
                일정_수행자는()
                        .일정이(PMS_등록기능)
                        .사용자가(코니)
                        .이다()
        );

        조회기능_담당 = taskUserRepository.save(
                일정_수행자는()
                        .일정이(PMS_조회기능)
                        .사용자가(레니)
                        .이다()
        );

        수정기능_담당 = taskUserRepository.save(
                일정_수행자는()
                        .일정이(PMS_수정기능)
                        .사용자가(레너드)
                        .이다()
        );
    }

    @Nested
    class 프로젝트_일정_수행자_목록_조회시 {

        @Test
        void 성공한다() {
            // given
            Long 프로젝트_ID = PMS_프로젝트.getId();
            Long 일정_ID = PMS_등록기능.getId();
            TaskUserResponse 예상_결과_항목_1 = 일정_수행자_목록_응답은()
                    .회원_식별자가(브라운.getId())
                    .닉네임이(브라운.getProfile().getNickname())
                    .역할_이름이(ADMIN.getName())
                    .이다();
            TaskUserResponse 예상_결과_항목_2 = 일정_수행자_목록_응답은()
                    .회원_식별자가(코니.getId())
                    .닉네임이(코니.getProfile().getNickname())
                    .역할_이름이(LEADER.getName())
                    .이다();
            List<TaskUserResponse> 예상_결과 = List.of(예상_결과_항목_1, 예상_결과_항목_2);

            // when
            List<TaskUserResponse> 실제_결과 = taskUserQueryRepository.getTaskUsersByProjectIdAndTaskId(프로젝트_ID,
                    일정_ID);

            // then
            assertThat(실제_결과).hasSize(예상_결과.size())
                    .extracting("userId", "nickname", "roleName")
                    .containsExactlyInAnyOrder(
                            tuple(예상_결과_항목_1.userId(), 예상_결과_항목_1.nickname(), 예상_결과_항목_1.roleName()),
                            tuple(예상_결과_항목_2.userId(), 예상_결과_항목_2.nickname(), 예상_결과_항목_2.roleName())
                    );
        }

        @Test
        void 프로젝트_식별자가_NULL_이면_예외가_발생한다() {
            // given
            Long 프로젝트_ID = null;
            Long 일정_ID = PMS_등록기능.getId();

            // when & then
            assertThatThrownBy(() -> taskUserQueryRepository.getTaskUsersByProjectIdAndTaskId(프로젝트_ID, 일정_ID))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void 프로젝트나_일정이_없으면_빈_리스트를_반환한다() {
            // given
            Long 잘못된_프로젝트_ID = Long.MIN_VALUE;
            Long 일정_ID = PMS_등록기능.getId();

            Long 프로젝트_ID = PMS_프로젝트.getId();
            Long 잘못된_일정_ID = Long.MIN_VALUE;

            // when
            List<TaskUserResponse> 잘못된_프로젝트_ID_결과 = taskUserQueryRepository.getTaskUsersByProjectIdAndTaskId(잘못된_프로젝트_ID, 일정_ID);
            List<TaskUserResponse> 잘못된_일정_ID_결과 = taskUserQueryRepository.getTaskUsersByProjectIdAndTaskId(프로젝트_ID, 잘못된_일정_ID);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(잘못된_프로젝트_ID_결과).isEmpty();
                softly.assertThat(잘못된_일정_ID_결과).isEmpty();
            });
        }
    }
}
