package com.growup.pms.team.service;

import static com.growup.pms.test.fixture.team.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.team.TeamUserTestBuilder.팀원은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.team.service.dto.TeamInvitationCreateCommand;
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
class TeamInvitationServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    TeamUserRepository teamUserRepository;

    @InjectMocks
    TeamInvitationService teamInvitationService;

    @Nested
    class 팀_초대시 {
        @Test
        void 성공한다() {
            // given
            String 초대된_역할 = TeamRole.LEADER.getRoleName();
            User 초대된_사용자 = 사용자는().식별자가(2L).이다();
            Team 초대할_팀 = 팀은().식별자가(1L).이다();
            TeamUser 초대된_팀원 = 팀원은().팀이(초대할_팀)
                    .사용자가(초대된_사용자)
                    .가입_대기_여부는(true)
                    .이다();

            when(teamUserRepository.existsById(any(TeamUserId.class))).thenReturn(false);
            when(teamUserRepository.save(any(TeamUser.class))).thenReturn(초대된_팀원);

            // when & then
            assertThatCode(() -> teamInvitationService.sendInvitation(초대할_팀.getId(),
                    new TeamInvitationCreateCommand(초대된_사용자.getId(), 초대된_역할)))
                    .doesNotThrowAnyException();
        }

        @Test
        void 초대할_사용자가_이미_팀에_있으면_예외가_발생한다() {
            // given
            String 초대된_역할 = TeamRole.LEADER.getRoleName();
            User 초대된_사용자 = 사용자는().식별자가(2L).이다();
            Team 초대할_팀 = 팀은().식별자가(1L).이다();

            when(teamUserRepository.existsById(any(TeamUserId.class))).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> teamInvitationService.sendInvitation(초대할_팀.getId(),
                    new TeamInvitationCreateCommand(초대된_사용자.getId(), 초대된_역할)))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_ALREADY_IN_TEAM);
        }

        @Test
        void 초대할_사용자가_존재하지_않으면_예외가_발생한다() {
            // given
            String 초대된_역할 = TeamRole.LEADER.getRoleName();
            Long 초대할_팀_ID = 1L;
            TeamInvitationCreateCommand 팀_초대_요청 = new TeamInvitationCreateCommand(초대할_팀_ID, 초대된_역할);

            when(teamUserRepository.existsById(any(TeamUserId.class))).thenReturn(false);
            doThrow(BusinessException.class).when(userRepository).findByIdOrThrow(any(Long.class));

            // when & then
            assertThatThrownBy(() -> teamInvitationService.sendInvitation(초대할_팀_ID, 팀_초대_요청))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class 팀_초대_승락시 {
        @Test
        void 성공한다() {
            // given
            Long 초대된_팀_ID = 1L;
            Long 초대된_사용자_ID = 2L;

            when(teamUserRepository.acceptInvitation(초대된_팀_ID, 초대된_사용자_ID)).thenReturn(1);

            // when & then
            assertThatCode(() -> teamInvitationService.acceptInvitation(초대된_팀_ID, 초대된_사용자_ID))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class 팀_초대_거절시 {
        @Test
        void 성공한다() {
            // given
            Long 초대된_팀_ID = 1L;
            Long 초대된_사용자_ID = 2L;

            // when
            when(teamUserRepository.declineInvitation(초대된_팀_ID, 초대된_사용자_ID)).thenReturn(1);

            // then
            // when & then
            assertThatCode(() -> teamInvitationService.declineInvitation(초대된_팀_ID, 초대된_사용자_ID))
                    .doesNotThrowAnyException();
        }
    }
}
