package com.growup.pms.invitation.service;

import static com.growup.pms.test.fixture.team.TeamInvitationTestBuilder.팀_초대는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.invitation.domian.TeamInvitation;
import com.growup.pms.invitation.dto.TeamInvitationCreateRequest;
import com.growup.pms.invitation.repository.TeamInvitationRepository;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TeamInvitationServiceTest {
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
            Long 초대할_사용자_ID = 1L;
            Long 예상하는_생성된_초대_ID = 1L;
            TeamInvitation 팀_초대 = 팀_초대는().식별자가(예상하는_생성된_초대_ID)
                    .팀_식별자가(초대할_팀_ID)
                    .초대할_사용자_식별자가(초대할_사용자_ID).이다();

            when(teamUserRepository.existsById(any(TeamUserId.class))).thenReturn(false);
            when(teamInvitationRepository.save(any(TeamInvitation.class))).thenReturn(팀_초대);

            // when
            Long 실제_생성된_초대_ID = teamInvitationService.sendInvitation(초대할_팀_ID, new TeamInvitationCreateRequest(초대할_사용자_ID));

            // then
            assertThat(실제_생성된_초대_ID).isEqualTo(예상하는_생성된_초대_ID);
        }

        @Test
        void 초대할_사용자가_이미_팀에_있으면_예외가_발생한다() {
            // given
            Long 초대할_팀_ID = 1L;
            Long 초대할_사용자_ID = 1L;
            TeamInvitationCreateRequest 팀_초대_요청 = new TeamInvitationCreateRequest(초대할_사용자_ID);

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
            TeamInvitationCreateRequest 팀_초대_요청 = new TeamInvitationCreateRequest(초대할_팀_ID);

            when(teamUserRepository.existsById(any(TeamUserId.class))).thenReturn(false);
            doThrow(DataIntegrityViolationException.class).when(teamInvitationRepository).save(any(TeamInvitation.class));

            // when & then
            assertThatThrownBy(() -> teamInvitationService.sendInvitation(초대할_팀_ID, 팀_초대_요청))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("ENTITY가 없습니다.");
        }
    }
}
