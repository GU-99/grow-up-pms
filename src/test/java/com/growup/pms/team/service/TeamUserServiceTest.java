package com.growup.pms.team.service;

import static com.growup.pms.test.fixture.role.RoleTestBuilder.역할은;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("팀원을 찾을 수 없습니다.");
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
                    .isInstanceOf(AuthorizationException.class)
                    .hasMessage("접근 권한이 없습니다.");
        }
    }
}
