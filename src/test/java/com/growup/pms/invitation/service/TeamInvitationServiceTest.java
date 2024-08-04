package com.growup.pms.invitation.service;

import static com.growup.pms.test.fixture.team.TeamInvitationTestBuilder.팀_초대는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.invitation.domian.TeamInvitation;
import com.growup.pms.invitation.dto.TeamInvitationCreateDto;
import com.growup.pms.invitation.repository.TeamInvitationRepository;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.fixture.team.TeamInvitationTestBuilder;
import com.growup.pms.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@WithMockSecurityUser(id = 1L)
@ExtendWith(MockitoExtension.class)
class TeamInvitationServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    TeamUserRepository teamUserRepository;

    @Mock
    TeamInvitationRepository teamInvitationRepository;

    @InjectMocks
    TeamInvitationService teamInvitationService;

    @Nested
    class 팀_초대시 {
        @Test
        void 성공한다() {
            // given
            Long 초대할_팀_ID = 1L;
            Long 초대할_사용자_ID = 2L;
            Long 예상하는_생성된_초대_ID = 1L;
            TeamInvitation 팀_초대 = 팀_초대는().식별자가(예상하는_생성된_초대_ID)
                    .팀_식별자가(초대할_팀_ID)
                    .초대할_사용자_식별자가(초대할_사용자_ID).이다();

            when(teamUserRepository.existsById(any(TeamUserId.class))).thenReturn(false);
            when(teamInvitationRepository.save(any(TeamInvitation.class))).thenReturn(팀_초대);

            // when
            Long 실제_생성된_초대_ID = teamInvitationService.sendInvitation(초대할_팀_ID, new TeamInvitationCreateDto(초대할_사용자_ID));

            // then
            assertThat(실제_생성된_초대_ID).isEqualTo(예상하는_생성된_초대_ID);
        }

        @Test
        void 초대할_사용자가_이미_팀에_있으면_예외가_발생한다() {
            // given
            Long 초대할_팀_ID = 1L;
            Long 초대할_사용자_ID = 2L;
            TeamInvitationCreateDto 팀_초대_요청 = new TeamInvitationCreateDto(초대할_사용자_ID);

            when(teamUserRepository.existsById(any(TeamUserId.class))).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> teamInvitationService.sendInvitation(초대할_팀_ID, 팀_초대_요청))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessage("사용자가 이미 팀에 존재합니다.");
        }

        @Test
        void 초대할_사용자가_존재하지_않으면_예외가_발생한다() {
            // given
            Long 초대할_팀_ID = 1L;
            TeamInvitationCreateDto 팀_초대_요청 = new TeamInvitationCreateDto(초대할_팀_ID);

            when(teamUserRepository.existsById(any(TeamUserId.class))).thenReturn(false);
            doThrow(EntityNotFoundException.class).when(userRepository).findByIdOrThrow(any(Long.class));

            // when & then
            assertThatThrownBy(() -> teamInvitationService.sendInvitation(초대할_팀_ID, 팀_초대_요청))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class 팀_초대_승락시 {
        @Test
        void 성공한다() {
            // given
            Long 초대된_팀_ID = 1L;
            Long 초대된_사용자_ID = 2L;
            Role 초대시_기본_역할 = new Role(TeamRole.MATE.getRoleName());
            TeamInvitation 팀_초대 = TeamInvitationTestBuilder.팀_초대는()
                    .초대할_사용자_식별자가(초대된_사용자_ID)
                    .팀_식별자가(초대된_사용자_ID)
                    .이다();

            when(teamInvitationRepository.findUserInvitationForTeam(초대된_팀_ID, 초대된_사용자_ID)).thenReturn(Optional.of(팀_초대));
            when(roleRepository.findByNameOrThrow(초대시_기본_역할.getName())).thenReturn(초대시_기본_역할);

            // when
            teamInvitationService.acceptInvitation(초대된_팀_ID, 초대된_사용자_ID);

            // then
            verify(teamInvitationRepository).delete(any(TeamInvitation.class));
            verify(teamUserRepository).save(any(TeamUser.class));
        }

        @Test
        void 해당_팀에_초대받지_못했는데_승낙을_하면_예외가_발생한다() {
            // given
            Long 초대된_팀_ID = 2L;
            Long 초대된_사용자_ID = 2L;

            doThrow(EntityNotFoundException.class).when(teamInvitationRepository).findUserInvitationForTeam(초대된_팀_ID, 초대된_사용자_ID);

            // when & then
            assertThatThrownBy(() -> teamInvitationService.acceptInvitation(초대된_팀_ID, 초대된_사용자_ID))
                    .isInstanceOf(EntityNotFoundException.class);
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
            teamInvitationService.declineInvitation(초대된_팀_ID, 초대된_사용자_ID);

            // then
            verify(teamInvitationRepository).declineUserInvitationForTeam(초대된_팀_ID, 초대된_사용자_ID);
        }
    }
}
