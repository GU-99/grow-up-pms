package com.growup.pms.common.aop.aspect;

import static com.growup.pms.test.fixture.auth.SecurityUserTestBuilder.인증된_사용자는;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.common.aop.annotation.RequirePermission;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.project.repository.ProjectUserRepository;
import com.growup.pms.role.domain.Permission;
import com.growup.pms.role.domain.PermissionType;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AuthorizationAspectTest {
    @Mock
    TeamUserRepository teamUserRepository;

    @Mock
    ProjectUserRepository projectUserRepository;

    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @InjectMocks
    AuthorizationAspect authorizationAspect;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    class 팀_권한을_체크시 {
        @Test
        void 성공한다() {
            // given
            Long 팀_ID = 1L;
            Long 사용자_ID = 1L;
            SecurityUser 인증된_유저 = 인증된_사용자는().식별자가(사용자_ID).이다();
            List<Permission> 부여된_권한 = List.of(new Permission(PermissionType.TEAM_DELETE.name()));
            RequirePermission 권한_애노테이션 = 필요한_권한이(PermissionType.TEAM_DELETE);

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(인증된_유저);
            when(teamUserRepository.getPermissionsForTeamUser(팀_ID, 사용자_ID)).thenReturn(부여된_권한);

            // when & then
            assertThatCode(() -> authorizationAspect.checkTeamPermission(권한_애노테이션, 팀_ID))
                    .doesNotThrowAnyException();
        }

        @Test
        void 필요한_권한이_없으면_예외가_발생한다() {
            // given
            Long 팀_ID = 1L;
            Long 사용자_ID = 1L;
            SecurityUser 인증된_유저 = 인증된_사용자는().식별자가(사용자_ID).이다();
            List<Permission> 부여된_권한 = Collections.emptyList();
            RequirePermission 권한_애노테이션 = 필요한_권한이(PermissionType.TEAM_DELETE);

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(인증된_유저);
            when(teamUserRepository.getPermissionsForTeamUser(팀_ID, 사용자_ID)).thenReturn(부여된_권한);

            // when & then
            assertThatThrownBy(() -> authorizationAspect.checkTeamPermission(권한_애노테이션, 팀_ID))
                    .isInstanceOf(AuthorizationException.class)
                    .hasMessage("접근 권한이 없습니다.");
        }
    }

    @Nested
    class 프로젝트_권한을_체크시 {
        @Test
        void 성공한다() {
            // given
            Long 프로젝트_ID = 1L;
            Long 사용자_ID = 1L;
            SecurityUser 인증된_유저 = 인증된_사용자는().식별자가(사용자_ID).이다();
            List<Permission> 부여된_권한 = List.of(new Permission(PermissionType.PROJECT_STATUS_WRITE.name()));
            RequirePermission 권한_애노테이션 = 필요한_권한이(PermissionType.PROJECT_STATUS_WRITE);

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(인증된_유저);
            when(projectUserRepository.getPermissionsForProjectUser(프로젝트_ID, 사용자_ID)).thenReturn(부여된_권한);

            // when & then
            assertThatCode(() -> authorizationAspect.checkProjectPermission(권한_애노테이션, 프로젝트_ID))
                    .doesNotThrowAnyException();
        }

        @Test
        void 필요한_권한이_없으면_예외가_발생한다() {
            // given
            Long 프로젝트_ID = 1L;
            Long 사용자_ID = 1L;
            SecurityUser 인증된_유저 = 인증된_사용자는().식별자가(사용자_ID).이다();
            List<Permission> 부여된_권한 = Collections.emptyList();
            RequirePermission 권한_애노테이션 = 필요한_권한이(PermissionType.PROJECT_STATUS_WRITE);

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(인증된_유저);
            when(projectUserRepository.getPermissionsForProjectUser(프로젝트_ID, 사용자_ID)).thenReturn(부여된_권한);

            // when & then
            assertThatThrownBy(() -> authorizationAspect.checkProjectPermission(권한_애노테이션, 프로젝트_ID))
                    .isInstanceOf(AuthorizationException.class)
                    .hasMessage("접근 권한이 없습니다.");
        }
    }

    private RequirePermission 필요한_권한이(PermissionType... permissions) {
        RequirePermission annotation = mock(RequirePermission.class);
        when(annotation.value()).thenReturn(permissions);
        return annotation;
    }
}
