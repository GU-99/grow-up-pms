package com.growup.pms.team.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.dto.TeamCreateRequest;
import com.growup.pms.team.dto.TeamResponse;
import com.growup.pms.team.dto.TeamUpdateRequest;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.team.TeamFixture;
import com.growup.pms.test.fixture.user.UserFixture;
import com.growup.pms.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.security.test.context.support.WithMockUser;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@WithMockUser
@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @Mock
    TeamRepository teamRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TeamService teamService;

    @Nested
    class 팀_생성_시에 {

        @Test
        void 성공한다() {
            // given
            Long creatorId = 1L;
            Long expectedTeamId = 1L;
            Team createdTeam = mock(Team.class);
            TeamCreateRequest request = TeamFixture.createDefaultTeamCreateRequest();

            when(userRepository.findByIdOrThrow(creatorId)).thenReturn(UserFixture.createUserWithId(creatorId));
            when(teamRepository.save(any(Team.class))).thenReturn(createdTeam);
            when(createdTeam.getId()).thenReturn(expectedTeamId);

            // when
            Long actualTeamId = teamService.createTeam(creatorId, request);

            // then
            assertThat(actualTeamId).isEqualTo(expectedTeamId);
        }

        @Test
        void 사용자가_존재하지_않으면_예외가_발생한다() {
            // given
            Long creatorId = 1L;
            TeamCreateRequest request = TeamFixture.createDefaultTeamCreateRequest();

            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)).when(userRepository).findByIdOrThrow(creatorId);

            // when & then
            assertThrows(EntityNotFoundException.class, () -> teamService.createTeam(creatorId, request));


        }
    }

    @Nested
    class 팀_조회_시에 {

        @Test
        void 성공한다() {
            // given
            Long teamId = 1L;
            Team team = TeamFixture.createTeamWithId(teamId);
            TeamResponse expectedResponse = TeamResponse.from(team);

            when(teamRepository.findByIdOrThrow(teamId)).thenReturn(team);

            // when
            TeamResponse actualResponse = teamService.getTeam(teamId);

            // then
            assertThat(actualResponse.getName()).isEqualTo(expectedResponse.getName());
            assertThat(actualResponse.getContent()).isEqualTo(expectedResponse.getContent());
        }

        @Test
        void 팀이_존재하지_않으면_예외가_발생한다() {
            // given
            Long teamId = 1L;

            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)).when(teamRepository).findByIdOrThrow(teamId);

            // when & then
            assertThrows(EntityNotFoundException.class, () -> teamService.getTeam(teamId));
        }
    }

    @Nested
    class 팀_변경_시에 {

        @Test
        void 성공한다() {
            // given
            Long teamId = 1L;
            Team team = TeamFixture.createTeamWithId(teamId);
            String newTeamName = "구구팔";
            String newTeamContent = "안녕하세요, 구구팔입니다!";
            TeamUpdateRequest request = TeamFixture.createDefaultTeamUpdateRequestBuilder()
                    .name(JsonNullable.of(newTeamName))
                    .content(JsonNullable.of(newTeamContent))
                    .build();

            when(teamRepository.findByIdOrThrow(teamId)).thenReturn(team);

            // when
            teamService.updateTeam(teamId, request);

            // then
            assertThat(team.getName()).isEqualTo(newTeamName);
            assertThat(team.getContent()).isEqualTo(newTeamContent);
        }
    }

    @Nested
    class 팀_삭제_시에 {

        @Test
        void 성공한다() {
            // given
            Long teamId = 1L;

            doNothing().when(teamRepository).deleteById(teamId);

            // when
            teamService.deleteTeam(teamId);

            // then
            verify(teamRepository).deleteById(teamId);
        }
    }
}
