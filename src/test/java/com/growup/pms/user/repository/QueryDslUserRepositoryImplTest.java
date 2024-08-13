package com.growup.pms.user.repository;

import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThat;

import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.RepositoryTestSupport;
import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.domain.User;
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
    QueryDslUserRepositoryImpl customizedUserRepository;

    User 브라운, 레니, 레너드;

    @BeforeEach
    void setUp() {
        브라운 = userRepository.save(사용자는().식별자가(1L).아이디가("brown").닉네임이("브라운").이다());
        레니 = userRepository.save(사용자는().식별자가(2L).아이디가("lenny").닉네임이("레니").이다());
        레너드 = userRepository.save(사용자는().식별자가(3L).아이디가("leonard").닉네임이("레너드").이다());
    }

    @Nested
    class 전체_사용자_검색시 {
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
}
