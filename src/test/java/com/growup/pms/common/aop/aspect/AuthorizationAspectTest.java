package com.growup.pms.common.aop.aspect;

import static com.growup.pms.test.fixture.auth.builder.SecurityUserTestBuilder.인증된_사용자는;
import static com.growup.pms.test.fixture.team.builder.TeamTestBuilder.팀은;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.ProjectId;
import com.growup.pms.common.aop.annotation.RequireProjectPermission;
import com.growup.pms.common.aop.annotation.RequireTeamPermission;
import com.growup.pms.common.aop.annotation.TeamId;
import com.growup.pms.common.util.AopUtil;
import com.growup.pms.project.service.ProjectService;
import com.growup.pms.role.domain.ProjectPermission;
import com.growup.pms.role.domain.TeamPermission;
import com.growup.pms.role.service.PermissionService;
import com.growup.pms.team.domain.Team;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import java.util.Optional;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AuthorizationAspectTest {

    @Mock
    ProjectService projectService;

    @Mock
    PermissionService permissionService;

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

        /**
         * Tests the successful team permission check scenario.
         *
         * This test verifies that the authorization aspect correctly checks team permissions
         * when a valid team ID is provided and the user has the required permissions.
         *
         * @throws Exception if any unexpected error occurs during permission checking
         */
        @Test
        void 성공한다() {
            try (MockedStatic<AopUtil> 헬퍼_클래스 = mockStatic(AopUtil.class)) {
                // given
                Long 팀_ID = 1L;
                Long 사용자_ID = 1L;
                SecurityUser 인증된_사용자 = 인증된_사용자는().식별자가(사용자_ID).이다();
                RequireTeamPermission 권한_애노테이션 = 필요한_팀_권한이(TeamPermission.DELETE_TEAM);

                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getPrincipal()).thenReturn(인증된_사용자);
                doNothing().when(permissionService).checkTeamPermission(eq(사용자_ID), eq(팀_ID), anyList());

                헬퍼_클래스.when(() -> AopUtil.findFirstAnnotatedParameterOfType(any(), eq(TeamId.class), eq(Long.class)))
                        .thenReturn(Optional.of(팀_ID));

                // when & then
                authorizationAspect.checkTeamPermission(mock(JoinPoint.class), 권한_애노테이션);
            }
        }

        /**
         * Tests the scenario where a team ID is not found, and the project ID is used to retrieve the associated team.
         *
         * This test verifies that when a team ID is not directly available, the authorization aspect
         * can successfully retrieve the team ID from an associated project. It checks the following flow:
         * 1. No direct team ID is found in the method parameters
         * 2. A project ID is successfully retrieved from the method parameters
         * 3. The associated team is fetched using the project ID
         * 4. Permission check is performed using the retrieved team ID
         *
         * @throws Exception if any unexpected error occurs during the permission check
         */
        @Test
        void 팀_ID를_찾지_못하면_프로젝트_ID_에서_가져온다() {
            try (MockedStatic<AopUtil> 헬퍼_클래스 = mockStatic(AopUtil.class)) {
                // given
                Long 팀_ID = 2L;
                Long 프로젝트_ID = 1L;
                Long 사용자_ID = 1L;
                Team 연관된_팀 = 팀은().식별자가(팀_ID).이다();
                SecurityUser 인증된_사용자 = 인증된_사용자는().식별자가(사용자_ID).이다();
                RequireTeamPermission 권한_애노테이션 = 필요한_팀_권한이(TeamPermission.DELETE_TEAM);

                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getPrincipal()).thenReturn(인증된_사용자);
                when(projectService.getAssociatedTeamForProject(프로젝트_ID)).thenReturn(연관된_팀);
                doNothing().when(permissionService).checkTeamPermission(eq(사용자_ID), eq(팀_ID), anyList());

                헬퍼_클래스.when(() -> AopUtil.findFirstAnnotatedParameterOfType(any(), eq(TeamId.class), eq(Long.class)))
                        .thenReturn(Optional.empty());
                헬퍼_클래스.when(() -> AopUtil.findFirstAnnotatedParameterOfType(any(), eq(ProjectId.class), eq(Long.class)))
                        .thenReturn(Optional.of(프로젝트_ID));

                // when & then
                authorizationAspect.checkTeamPermission(mock(JoinPoint.class), 권한_애노테이션);
            }
        }

        /**
         * 팀 ID와 프로젝트 ID가 모두 없을 경우 예외를 발생시키는 테스트 메서드.
         *
         * 이 테스트는 AOP 유틸리티 클래스에서 팀 또는 프로젝트 ID를 찾을 수 없는 상황을 검증한다.
         * AopUtil의 findFirstAnnotatedParameterOfType 메서드가 빈 Optional을 반환할 때,
         * checkTeamPermission 메서드가 적절한 예외를 던지는지 확인한다.
         *
         * @throws IllegalStateException 팀 ID나 프로젝트 ID를 가진 파라미터를 찾을 수 없을 경우
         */
        @Test
        void 팀_ID와_프로젝트_ID_모두_없으면_예외가_발생한다() {
            try (MockedStatic<AopUtil> 헬퍼_클래스 = mockStatic(AopUtil.class)) {
                // given
                헬퍼_클래스.when(() -> AopUtil.findFirstAnnotatedParameterOfType(any(), any(), any())).thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> authorizationAspect.checkTeamPermission(mock(JoinPoint.class), mock(RequireTeamPermission.class)))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("'TeamId' 혹은 'ProjectId' 애노테이션이 붙은 'Long' 타입의 파라미터를 찾을 수 없습니다.");
            }
        }
    }

    @Nested
    class 프로젝트_권한을_체크시 {

        /**
         * Tests the successful project permission check for a user.
         *
         * This test verifies that when a user has the required project permission,
         * the authorization aspect allows the operation to proceed without throwing an exception.
         *
         * @throws Exception if any unexpected error occurs during the permission check
         */
        @Test
        void 성공한다() {
            try (MockedStatic<AopUtil> 헬퍼_클래스 = mockStatic(AopUtil.class)) {
                // given
                Long 프로젝트_ID = 1L;
                Long 사용자_ID = 1L;
                SecurityUser 인증된_사용자 = 인증된_사용자는().식별자가(사용자_ID).이다();
                RequireProjectPermission 권한_애노테이션 = 필요한_프로젝트_권한이(ProjectPermission.UPDATE_STATUS);

                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getPrincipal()).thenReturn(인증된_사용자);
                doNothing().when(permissionService).checkProjectPermission(eq(사용자_ID), eq(프로젝트_ID), anyList());

                헬퍼_클래스.when(() -> AopUtil.findFirstAnnotatedParameterOfTypeOrThrow(any(), eq(ProjectId.class), eq(Long.class)))
                        .thenReturn(프로젝트_ID);

                // when & then
                authorizationAspect.checkProjectPermission(mock(JoinPoint.class), 권한_애노테이션);
            }
        }
    }

    /**
     * Creates a mock {@code RequireTeamPermission} annotation with specified team permissions.
     *
     * @param permissions Variable number of team permissions to be set on the mock annotation
     * @return A mocked {@code RequireTeamPermission} annotation configured with the given permissions
     */
    private RequireTeamPermission 필요한_팀_권한이(TeamPermission... permissions) {
        RequireTeamPermission annotation = mock(RequireTeamPermission.class);
        when(annotation.value()).thenReturn(permissions);
        return annotation;
    }

    /**
     * Creates a mock {@code RequireProjectPermission} annotation with specified project permissions.
     *
     * @param permissions Variable number of {@code ProjectPermission} enum values to be set on the mock annotation
     * @return A mocked {@code RequireProjectPermission} annotation configured with the given permissions
     */
    private RequireProjectPermission 필요한_프로젝트_권한이(ProjectPermission... permissions) {
        RequireProjectPermission annotation = mock(RequireProjectPermission.class);
        when(annotation.value()).thenReturn(permissions);
        return annotation;
    }
}
