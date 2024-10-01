package com.growup.pms.project.service;

import static com.growup.pms.test.fixture.project.builder.ProjectTestBuilder.프로젝트는;
import static com.growup.pms.test.fixture.project.builder.ProjectUserCreateRequestTestBuilder.프로젝트_유저_생성_요청은;
import static com.growup.pms.test.fixture.role.builder.RoleTestBuilder.역할은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.project.domain.Project;
import com.growup.pms.project.domain.ProjectUserId;
import com.growup.pms.project.repository.ProjectRepository;
import com.growup.pms.project.repository.ProjectUserRepository;
import com.growup.pms.project.service.dto.ProjectUserCreateCommand;
import com.growup.pms.role.domain.ProjectRole;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.RoleType;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class ProjectUserServiceTest {

    @Mock
    ProjectUserRepository projectUserRepository;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    ProjectUserService projectUserService;

    @Nested
    class 사용자가_프로젝트원_추가_시에 {

        @Test
        void 성공한다() {
            // given
            Long projectId = 1L;
            ProjectUserCreateCommand command = 프로젝트_유저_생성_요청은().역할_이름이(ProjectRole.ADMIN.getRoleName())
                    .이다().toCommand();

            Project project = 프로젝트는().이다();
            User user = 사용자는().이다();
            Role role = 역할은()
                    .타입이(RoleType.PROJECT)
                    .이름이(ProjectRole.ADMIN.getRoleName())
                    .이다();

            when(projectUserRepository.existsById(any(ProjectUserId.class))).thenReturn(false);
            when(projectRepository.findByIdOrThrow(projectId))
                    .thenReturn(project);
            when(userRepository.findByIdOrThrow(command.userId()))
                    .thenReturn(user);
            when(roleRepository.findProjectRoleByName(command.roleName()))
                    .thenReturn(role);

            // when & then
            assertThatCode(() -> projectUserService.createProjectUser(projectId, command))
                    .doesNotThrowAnyException();
        }

        @Test
        void 프로젝트에_이미_존재하는_회원이면_예외가_발생한다() {
            // given
            Long projectId = 1L;
            ProjectUserCreateCommand command = 프로젝트_유저_생성_요청은().역할_이름이(ProjectRole.ADMIN.getRoleName())
                    .이다().toCommand();

            // when
            doThrow(BusinessException.class).when(projectUserRepository).existsById(any(ProjectUserId.class));

            // then
            assertThatThrownBy(() -> projectUserService.createProjectUser(projectId, command))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void 권한이_없는_사용자면_예외가_발생한다() {
            // given
            Long projectId = 1L;
            ProjectUserCreateCommand command = 프로젝트_유저_생성_요청은().역할_이름이(ProjectRole.ASSIGNEE.getRoleName())
                    .이다().toCommand();

            when(projectUserRepository.existsById(any(ProjectUserId.class))).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> projectUserService.createProjectUser(projectId, command))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void 회원이_존재하지_않으면_예외가_발생한다() {
            // given
            Long projectId = 1L;
            ProjectUserCreateCommand command = 프로젝트_유저_생성_요청은().역할_이름이(ProjectRole.ADMIN.getRoleName())
                    .이다().toCommand();

            when(projectUserRepository.existsById(any(ProjectUserId.class))).thenReturn(false);
            doThrow(BusinessException.class).when(userRepository).findByIdOrThrow(command.userId());

            // when & then
            assertThatThrownBy(() -> projectUserService.createProjectUser(projectId, command))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void 권한이_존재하지_않으면_예외가_발생한다() {
            // given
            Long projectId = 1L;
            ProjectUserCreateCommand command = 프로젝트_유저_생성_요청은().역할_이름이(ProjectRole.ADMIN.getRoleName())
                    .이다().toCommand();

            when(projectUserRepository.existsById(any(ProjectUserId.class))).thenReturn(false);
            doThrow(BusinessException.class).when(roleRepository).findProjectRoleByName(command.roleName());

            // when & then
            assertThatThrownBy(() -> projectUserService.createProjectUser(projectId, command))
                    .isInstanceOf(BusinessException.class);
        }
    }
}
