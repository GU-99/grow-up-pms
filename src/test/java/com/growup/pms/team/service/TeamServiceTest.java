package com.growup.pms.team.service;

import static com.growup.pms.test.fixture.team.builder.TeamCreateRequestTestBuilder.팀_생성_요청은;
import static com.growup.pms.test.fixture.team.builder.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.team.builder.TeamUpdateRequestTestBuilder.팀_변경_요청은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.project.service.ProjectService;
import com.growup.pms.team.controller.dto.response.TeamResponse;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.team.service.dto.TeamCreateCommand;
import com.growup.pms.team.service.dto.TeamUpdateCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
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
class TeamServiceTest {
    @Mock
    TeamRepository teamRepository;

    @Mock
    TeamUserRepository teamUserRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ProjectService projectService;

    @InjectMocks
    TeamService teamService;

    @Nested
    class 팀_생성_시에 {
        @Test
        void 성공한다() {
            // given
            Long 팀장_ID = 1L;
            Long 예상_팀_ID = 1L;
            Team 생성된_팀 = mock(Team.class);
            TeamCreateCommand 팀_생성_요청 = 팀_생성_요청은().이다().toCommand();

            when(userRepository.findByIdOrThrow(팀장_ID)).thenReturn(사용자는().식별자가(팀장_ID).이다());
            when(teamRepository.save(any(Team.class))).thenReturn(생성된_팀);
            when(생성된_팀.getId()).thenReturn(예상_팀_ID);

            // when
            Long 실제_팀_ID = teamService.createTeam(팀장_ID, 팀_생성_요청);

            // then
            assertThat(실제_팀_ID).isEqualTo(예상_팀_ID);
        }

        @Test
        void 사용자가_존재하지_않으면_예외가_발생한다() {
            // given
            Long 팀장_ID = 1L;
            TeamCreateCommand 팀_생성_요청 = 팀_생성_요청은().이다().toCommand();

            doThrow(new BusinessException(ErrorCode.USER_NOT_FOUND)).when(userRepository).findByIdOrThrow(팀장_ID);

            // when & then
            assertThatThrownBy(() -> teamService.createTeam(팀장_ID, 팀_생성_요청))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class 팀_조회_시에 {
        @Test
        void 성공한다() {
            // given
            Long 예상_팀_ID = 1L;
            Team 기존_팀 = 팀은().식별자가(예상_팀_ID).이다();
            TeamResponse 예상_결과 = TeamResponse.from(기존_팀);

            when(teamRepository.findByIdOrThrow(예상_팀_ID)).thenReturn(기존_팀);

            // when
            TeamResponse 실제_결과 = teamService.getTeam(예상_팀_ID);

            // then
            assertSoftly(softly -> {
                softly.assertThat(실제_결과.teamName()).isEqualTo(예상_결과.teamName());
                softly.assertThat(실제_결과.content()).isEqualTo(예상_결과.content());
            });
        }

        @Test
        void 팀이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 존재하지_않는_팀_ID = 1L;

            doThrow(new BusinessException(ErrorCode.TEAM_NOT_FOUND)).when(teamRepository).findByIdOrThrow(존재하지_않는_팀_ID);

            // when & then
            assertThatThrownBy(() -> teamService.getTeam(존재하지_않는_팀_ID))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class 팀_변경_시에 {
        @Test
        void 성공한다() {
            // given
            Long 기존_팀_ID = 1L;
            Team 기존_팀 = 팀은().식별자가(기존_팀_ID).이다();
            String 새로운_팀_이름 = "구구팔";
            String 새로운_팀_소개 = "안녕하세요, 구구팔입니다!";
            TeamUpdateCommand request = 팀_변경_요청은().이름이(새로운_팀_이름).소개가(새로운_팀_소개).이다().toCommand();

            when(teamRepository.findByIdOrThrow(기존_팀_ID)).thenReturn(기존_팀);

            // when
            teamService.updateTeam(기존_팀_ID, request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(기존_팀.getName()).isEqualTo(새로운_팀_이름);
                softly.assertThat(기존_팀.getContent()).isEqualTo(새로운_팀_소개);
            });
        }
    }

    @Nested
    class 팀_탈퇴_시에 {
        @Test
        void 사용자가_탈퇴에_성공한다() {
            // given
            Long 팀_ID = 1L;
            Long 사용자_ID = 1L;
            boolean 팀장_여부 = false;

            when(teamRepository.isUserTeamLeader(팀_ID, 사용자_ID)).thenReturn(팀장_여부);
            doNothing().when(teamUserRepository).deleteById(new TeamUserId(팀_ID, 사용자_ID));

            // when & then
            assertThatCode(() -> teamService.leaveTeam(팀_ID, 사용자_ID))
                    .doesNotThrowAnyException();
        }

        @Test
        void 팀장이_탈퇴에_성공한다() {
            // given
            Long 팀_ID = 1L;
            Long 사용자_ID = 1L;
            boolean 팀장_여부 = true;

            when(teamRepository.isUserTeamLeader(팀_ID, 사용자_ID)).thenReturn(팀장_여부);
            doNothing().when(teamUserRepository).deleteAllByTeamId(팀_ID);
            doNothing().when(projectService).deleteAllProjectsForTeam(팀_ID);

            // when & then
            assertThatCode(() -> teamService.leaveTeam(팀_ID, 사용자_ID))
                    .doesNotThrowAnyException();
        }
    }
}
