package com.growup.pms.user.repository;

import static com.growup.pms.test.fixture.role.RoleTestBuilder.역할은;
import static com.growup.pms.test.fixture.team.TeamTestBuilder.팀은;
import static com.growup.pms.test.fixture.team.TeamUserTestBuilder.팀원은;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;

import com.growup.pms.role.domain.Role;
import com.growup.pms.role.repository.RoleRepository;
import com.growup.pms.team.domain.Team;
import com.growup.pms.team.repository.TeamRepository;
import com.growup.pms.team.repository.TeamUserRepository;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.RepositoryTestSupport;
import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import com.growup.pms.user.domain.User;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class QueryDslUserRepositoryImplTest extends RepositoryTestSupport {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamUserRepository teamUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    QueryDslUserRepositoryImpl customizedUserRepository;

    User 브라운, 레니, 레너드;

    @BeforeEach
    void setUp() {
        브라운 = userRepository.save(사용자는().식별자가(1L).아이디가("brown").닉네임이("브라운").이다());
        레니 = userRepository.save(사용자는().식별자가(2L).아이디가("lenny").닉네임이("레니").이다());
        레너드 = userRepository.save(사용자는().식별자가(3L).아이디가("leonard").닉네임이("레너드").이다());
    }

    @Nested
    class 전체_사용자_검색_시 {
        @Test
        void 성공한다() {
            // given
            String 닉네임_접두사 = "레";

            // when
            List<UserSearchResponse> 실제_결과 = customizedUserRepository.findUsersByNicknameStartingWith(닉네임_접두사);

            // then
            assertThat(실제_결과.stream().map(UserSearchResponse::nickname))
                    .containsExactlyInAnyOrder("레니", "레너드");
        }

        @Test
        void 매칭되는_사용자가_없으면_빈_리스트를_반환한다() {
            // given
            String 닉네임_접두사 = "타";

            // when
            List<UserSearchResponse> 실제_결과 = customizedUserRepository.findUsersByNicknameStartingWith(닉네임_접두사);

            // then
            assertThat(실제_결과).isEmpty();
        }
    }

    @Nested
    class 가입된_팀_목록_조회_시 {
        public static final String ROLE_TEAM_ADMIN = "TEAM_ADMIN";
        public static final String ROLE_TEAM_MATE = "TEAM_MATE";

        Role 관리자_역할, 메이트_역할;
        Team 브라운_팀, 레니_팀, 레너드_팀;

        @BeforeEach
        void setUp() {
            관리자_역할 = roleRepository.save(역할은().이름이(ROLE_TEAM_ADMIN).이다());
            메이트_역할 = roleRepository.save(역할은().이름이(ROLE_TEAM_MATE).이다());

            브라운_팀 = 사용자가_새로운_팀을_생성한다(브라운);
            레니_팀 = 사용자가_새로운_팀을_생성한다(레니);
            레너드_팀 = 사용자가_새로운_팀을_생성한다(레너드);
        }

        @Test
        void 성공한다() {
            // given
            Long 사용자_ID = 브라운.getId();

            사용자가_팀에_가입한_상태다(브라운, 레니_팀);
            사용자가_팀에_가입_대기_중이다(브라운, 레너드_팀);

            List<UserTeamResponse> 예상_결과 = Arrays.asList(
                    팀에_가입한_상태일_것으로_예상한다(브라운_팀),
                    팀에_가입한_상태일_것으로_예상한다(레니_팀),
                    팀에_가입_대기_중인_상태일_것으로_예상한다(레너드_팀)
            );

            // when
            List<UserTeamResponse> 실제_결과 = customizedUserRepository.findAllUserTeams(사용자_ID);

            // then
            assertThat(실제_결과).usingRecursiveComparison().isEqualTo(예상_결과);
        }

        Team 사용자가_새로운_팀을_생성한다(User 사용자) {
            Team 새로운_팀 = teamRepository.save(팀은().식별자가(null).팀장이(사용자).이름이(사용자.getProfile().getNickname()).이다());
            teamUserRepository.save(팀원은().팀이(새로운_팀).사용자가(사용자).역할이(관리자_역할).가입_대기_여부는(false).이다());
            return 새로운_팀;
        }

        void 사용자가_팀에_가입한_상태다(User 사용자, Team 가입한_팀) {
            teamUserRepository.save(팀원은().팀이(가입한_팀).사용자가(사용자).역할이(메이트_역할).가입_대기_여부는(false).이다());
        }

        void 사용자가_팀에_가입_대기_중이다(User 사용자, Team 가입_신청한_팀) {
            teamUserRepository.save(팀원은().팀이(가입_신청한_팀).사용자가(사용자).역할이(메이트_역할).가입_대기_여부는(true).이다());
        }

        UserTeamResponse 팀에_가입한_상태일_것으로_예상한다(Team 가입한_팀) {
            return new UserTeamResponse(가입한_팀.getId(), 가입한_팀.getName(), 가입한_팀.getContent(),
                    가입한_팀.getCreator().getProfile().getNickname(), false, ROLE_TEAM_MATE);
        }

        UserTeamResponse 팀에_가입_대기_중인_상태일_것으로_예상한다(Team 가입한_팀) {
            return new UserTeamResponse(가입한_팀.getId(), 가입한_팀.getName(), 가입한_팀.getContent(),
                    가입한_팀.getCreator().getProfile().getNickname(), true, ROLE_TEAM_MATE);
        }
    }
}
