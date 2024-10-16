package com.growup.pms.team.service;

import static com.growup.pms.test.fixture.role.builder.RoleTestBuilder.역할은;
import static com.growup.pms.test.fixture.team.builder.TeamUserResponseTestBuilder.팀원_응답은;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.team.controller.dto.response.TeamUserResponse;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TeamUserServiceTest {

    @Mock
    TeamUserRepository teamUserRepository;

    @InjectMocks
    TeamUserService teamUserService;

    @Nested
    class 팀_유저_조회_시 {

        @Test
        void 성공한다() {
            // given
            Long 팀_ID = 1L;
            List<TeamUserResponse> 예상_결과 = List.of(
                    팀원_응답은().사용자_식별자가(1L).닉네임이("브라운").역할명이(TeamRole.HEAD.getRoleName()).이다(),
                    팀원_응답은().사용자_식별자가(2L).닉네임이("코니").역할명이(TeamRole.MATE.getRoleName()).이다());

            when(teamUserRepository.getAllTeamUsers(팀_ID)).thenReturn(예상_결과);

            // when
            List<TeamUserResponse> 실제_결과 = teamUserService.getAllTeamUsers(팀_ID);

            // then
            assertThat(실제_결과).isEqualTo(예상_결과);
        }
    }

    @Nested
    class 팀_유저_추방_시 {

        @Test
        void 성공한다() {
            // given
            Long 팀_ID = 1L;
            Long 추방할_팀원_ID = 3L;
            Role 추방할_팀원_역할 = 역할은().이름이(TeamRole.MATE.getRoleName()).이다();

            when(teamUserRepository.findRoleById(팀_ID, 추방할_팀원_ID)).thenReturn(Optional.of(추방할_팀원_역할));
            doNothing().when(teamUserRepository).deleteById(any(TeamUserId.class));

            // when & then
            assertThatCode(() -> teamUserService.kickMember(팀_ID, 추방할_팀원_ID))
                    .doesNotThrowAnyException();
        }

        @Test
        void 추방할_팀원이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 팀_ID = 1L;
            Long 추방할_팀원_ID = 3L;

            when(teamUserRepository.findRoleById(팀_ID, 추방할_팀원_ID)).thenReturn(Optional.empty());

            // when & then
            assertThatCode(() -> teamUserService.kickMember(팀_ID, 추방할_팀원_ID))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TEAM_MEMBER_NOT_FOUND);
        }

        @Test
        void 추방할_팀원의_역할이_Mate가_아니면_예외가_발생한다() {
            // given
            Long 팀_ID = 1L;
            Long 추방할_팀원_ID = 3L;
            Role 추방할_팀원_역할 = 역할은().이름이(TeamRole.LEADER.getRoleName()).이다();

            when(teamUserRepository.findRoleById(팀_ID, 추방할_팀원_ID)).thenReturn(Optional.of(추방할_팀원_역할));

            // when & then
            assertThatCode(() -> teamUserService.kickMember(팀_ID, 추방할_팀원_ID))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ACCESS_DENIED);
        }
    }

    @Nested
    class 팀원_역할_변경_시 {

        @ParameterizedTest
        @EnumSource(value = TeamRole.class, names = {"LEADER", "MATE"})
        void 성공한다(TeamRole 새로운_역할) {
            // given
            Long 팀_ID = 1L;
            Long 역할_변경할_팀원_ID = 2L;
            Long 변경된_레코드_수 = 1L;
            String 새로운_역할명 = 새로운_역할.getRoleName();

            when(teamUserRepository.updateRoleForTeamUser(팀_ID, 역할_변경할_팀원_ID, 새로운_역할명)).thenReturn(변경된_레코드_수);

            // when & then
            assertThatCode(() -> teamUserService.changeRole(팀_ID, 역할_변경할_팀원_ID, 새로운_역할명))
                    .doesNotThrowAnyException();
        }

        @Test
        void 팀원을_관리자로_지정하려는_경우_예외가_발생한다() {
            // given
            Long 팀_ID = 1L;
            Long 역할_변경할_팀원_ID = 2L;
            String 변경할_역할명 = TeamRole.HEAD.getRoleName();

            // when & then
            assertThatThrownBy(() -> teamUserService.changeRole(팀_ID, 역할_변경할_팀원_ID, 변경할_역할명))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.UNAUTHORIZED_ROLE_ASSIGNMENT);
        }
    }
}
