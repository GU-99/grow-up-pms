package com.growup.pms.team.service;

import static com.growup.pms.test.fixture.team.TeamCreateRequestTestBuilder.팀_생성_요청은;
import static com.growup.pms.test.fixture.team.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.team.TeamUpdateRequestTestBuilder.팀_수정_요청은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
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
import com.growup.pms.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
            Long 팀장_ID = 1L;
            Long 예상_팀_ID = 1L;
            Team 생성된_팀 = mock(Team.class);
            TeamCreateRequest 팀_생성_요청 = 팀_생성_요청은().이다();

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
            TeamCreateRequest 팀_생성_요청 = 팀_생성_요청은().이다();

            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)).when(userRepository).findByIdOrThrow(팀장_ID);

            // when & then
            assertThatThrownBy(() -> teamService.createTeam(팀장_ID, 팀_생성_요청))
                    .isInstanceOf(EntityNotFoundException.class);
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
                softly.assertThat(실제_결과.getName()).isEqualTo(예상_결과.getName());
                softly.assertThat(실제_결과.getContent()).isEqualTo(예상_결과.getContent());
            });
        }

        @Test
        void 팀이_존재하지_않으면_예외가_발생한다() {
            // given
            Long 존재하지_않는_팀_ID = 1L;

            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)).when(teamRepository).findByIdOrThrow(존재하지_않는_팀_ID);

            // when & then
            assertThatThrownBy(() -> teamService.getTeam(존재하지_않는_팀_ID))
                    .isInstanceOf(EntityNotFoundException.class);
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
            TeamUpdateRequest request = 팀_수정_요청은().이름이(새로운_팀_이름).소개가(새로운_팀_소개).이다();

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
    class 팀_삭제_시에 {
        @Test
        void 성공한다() {
            // given
            Long 기존_팀_ID = 1L;

            doNothing().when(teamRepository).deleteById(기존_팀_ID);

            // when
            teamService.deleteTeam(기존_팀_ID);

            // then
            verify(teamRepository).deleteById(기존_팀_ID);
        }
    }
}
