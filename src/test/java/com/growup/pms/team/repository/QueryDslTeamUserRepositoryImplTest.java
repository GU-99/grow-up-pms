package com.growup.pms.team.repository;


import static com.growup.pms.role.domain.RoleType.TEAM;
import static com.growup.pms.role.domain.TeamRole.HEAD;
import static com.growup.pms.role.domain.TeamRole.LEADER;
import static com.growup.pms.role.domain.TeamRole.MATE;
import static com.growup.pms.test.fixture.role.builder.RoleTestBuilder.역할은;
import static com.growup.pms.test.fixture.team.builder.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.team.builder.TeamUserTestBuilder.팀원은;
import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.controller.dto.response.TeamUserSearchResponse;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.domain.TeamUser;
import com.growup.pms.team.domain.TeamUserId;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.RepositoryTestSupport;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class QueryDslTeamUserRepositoryImplTest extends RepositoryTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamUserRepository teamUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    QueryDslTeamUserRepositoryImpl queryDslTeamUserRepository;

    Role 어드민, 리더, 메이트;
    User 브라운;
    Team 브라운_팀;
    TeamUser 브라운_팀장;

    @BeforeEach
    void setUp() {
        어드민 = roleRepository.save(역할은().식별자가(1L).타입이(TEAM).이름이(HEAD.getRoleName()).이다());
        리더 = roleRepository.save(역할은().식별자가(2L).타입이(TEAM).이름이(LEADER.getRoleName()).이다());
        메이트 = roleRepository.save(역할은().식별자가(3L).타입이(TEAM).이름이(MATE.getRoleName()).이다());

        브라운 = userRepository.save(사용자는().아이디가("brown").닉네임이("브라운").이다());
        브라운_팀 = teamRepository.save(팀은().이름이("브라운 팀").팀장이(브라운).이다());
        브라운_팀장 = teamUserRepository.save(팀원은().팀이(브라운_팀).사용자가(브라운).역할이(어드민).이다());
    }

    @Nested
    class 역할_변경_시 {

        @ParameterizedTest
        @EnumSource(names = {"LEADER", "MATE"}, value = TeamRole.class)
        void 성공한다(TeamRole 새_역할) {
            // when
            long 변경_수 = queryDslTeamUserRepository.updateRoleForTeamUser(브라운_팀.getId(), 브라운.getId(), 새_역할.getRoleName());
            entityManager.clear();

            // then
            assertSoftly(softly -> {
                브라운_팀장 = teamUserRepository.findById(new TeamUserId(브라운_팀.getId(), 브라운.getId())).orElseThrow();
                assertThat(변경_수).isEqualTo(1);
                assertThat(브라운_팀장.getRole().getName()).isEqualTo(새_역할.getRoleName());
            });
        }

        @Test
        void 존재하지_않는_역할을_부여하려고_하면_예외가_발생한다() {
            // given
            String 존재하지_않는_역할 = "UNKNOWN";

            // when & then
            assertThatThrownBy(() -> queryDslTeamUserRepository.updateRoleForTeamUser(브라운_팀.getId(), 브라운.getId(), 존재하지_않는_역할))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 역할은 존재하지 않습니다: %s".formatted(존재하지_않는_역할));
        }
    }

    @Nested
    class 팀원_검색_시 {

        @Test
        void 성공한다() {
            // given
            Long 사용자_ID = 10L;
            String 닉네임_접두사 = "브";

            // when
            List<TeamUserSearchResponse> 실제_결과 = queryDslTeamUserRepository.getTeamUsersByNicknameStartingWith(
                    사용자_ID, 브라운_팀.getId(), 닉네임_접두사);

            // then
            assertThat(실제_결과.stream().map(TeamUserSearchResponse::nickname))
                    .containsExactlyInAnyOrder("브라운");
        }

        @Test
        void 매칭되는_사용자가_없으면_빈_리스트를_반환한다() {
            // given
            Long 사용자_ID = 10L;
            String 닉네임_접두사 = "타";

            // when
            List<TeamUserSearchResponse> 실제_결과 = queryDslTeamUserRepository.getTeamUsersByNicknameStartingWith(
                    사용자_ID, 브라운_팀.getId(), 닉네임_접두사);

            // then
            assertThat(실제_결과).isEmpty();
        }

        @Test
        void 검색_결과에_자신이_포함되어_있으면_제외한다() {
            // given
            Long 사용자_ID = 브라운.getId();
            String 닉네임_접두사 = "브";

            // when
            List<TeamUserSearchResponse> 실제_결과 = queryDslTeamUserRepository.getTeamUsersByNicknameStartingWith(
                    사용자_ID, 브라운_팀.getId(), 닉네임_접두사);

            // then
            assertThat(실제_결과).isEmpty();
        }
    }
}
