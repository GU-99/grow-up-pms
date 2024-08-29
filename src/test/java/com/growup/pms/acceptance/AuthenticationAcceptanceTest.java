package com.growup.pms.acceptance;

import static com.growup.pms.test.fixture.auth.LoginRequestTestBuilder.로그인_하는_사용자는;
import static com.growup.pms.test.fixture.user.UserTestBuilder.사용자는;
import static com.growup.pms.test.fixture.user.data.UserTestData.라이언;
import static com.growup.pms.test.fixture.user.data.UserTestData.소피아;
import static com.growup.pms.test.fixture.user.data.UserTestData.알렉스;
import static com.growup.pms.test.fixture.user.data.UserTestData.에밀리;
import static com.growup.pms.test.support.rest.AuthenticationAcceptanceTestAction.리프레시_토큰_쿠키_이름;
import static com.growup.pms.test.support.rest.AuthenticationAcceptanceTestAction.인증_정보를_가져온다;
import static com.growup.pms.test.support.rest.AuthenticationAcceptanceTestAction.인증되지_않은_사용자가_POST_요청을_보낸다;
import static com.growup.pms.test.support.rest.AuthenticationAcceptanceTestAction.인증된_사용자가_POST_요청을_보낸다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.dto.ErrorResponse;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.AcceptanceTestSupport;
import com.growup.pms.user.domain.User;
import io.restassured.response.ExtractableResponse;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
public class AuthenticationAcceptanceTest extends AcceptanceTestSupport {

    @Nested
    class 사용자가_일반_로그인_시에 {

        @Test
        void 계정이_존재하고_아이디와_비밀번호가_일치하면_성공한다() {
            // given
            사용자가_새로_가입한다(에밀리());

            // when
            var 실제_응답 = 사용자가_로그인을_시도한다(에밀리());

            // then
            assertSoftly(softly -> {
                softly.assertThat(실제_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(실제_응답.cookie(리프레시_토큰_쿠키_이름)).isNotEmpty();
                softly.assertThat(실제_응답.header(HttpHeaders.AUTHORIZATION)).isNotEmpty();
            });
        }

        @Test
        void 계정이_존재하고_아이디와_비밀번호가_일치하지_않으면_실패한다() {
            // given
            var 에밀리_아이디 = "emily_e";
            var 에밀리 = 사용자는().아이디가(에밀리_아이디).비밀번호가("emily@1234!").이다();
            var 틀린_계정_정보 = 사용자는().아이디가(에밀리_아이디).비밀번호가("!4321@ylime").이다();

            사용자가_새로_가입한다(에밀리);

            // when
            var 실제_응답 = 사용자가_로그인을_시도한다(틀린_계정_정보);
            var 에러_결과 = 실제_응답.as(ErrorResponse.class);

            // then
            assertSoftly(softly -> {
                softly.assertThat(실제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                softly.assertThat(에러_결과).usingRecursiveComparison()
                        .isEqualTo(ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILED));
            });
        }

        @Test
        void 계정이_존재하지_않으면_실패한다() {
            // when
            var 실제_응답 = 사용자가_로그인을_시도한다(에밀리());
            var 에러_결과 = 실제_응답.as(ErrorResponse.class);

            // then
            assertSoftly(softly -> {
                softly.assertThat(실제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                softly.assertThat(에러_결과).usingRecursiveComparison()
                        .isEqualTo(ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILED));
            });
        }
    }

    @Nested
    class 사용자가_로그아웃_할_때 {

        @Test
        void 성공한다() {
            // given
            사용자가_새로_가입한다(알렉스());

            var 인증_응답 = 사용자가_로그인을_시도한다(알렉스());
            var 인증_토큰 = 인증_정보를_가져온다(인증_응답);

            // when
            var 실제_응답 = 인증된_사용자가_POST_요청을_보낸다(인증_토큰, "/api/v1/user/logout");

            // then
            assertThat(실제_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        void 리프레시_토큰_없이_요청을_보내면_실패한다() {
            // when
            var 실제_응답 = 인증되지_않은_사용자가_POST_요청을_보낸다("/api/v1/user/logout");

            // then
            assertThat(실제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @Nested
    class 사용자가_토큰을_재발급_할_때 {

        @Test
        void 성공한다() {
            // given
            사용자가_새로_가입한다(소피아());

            var 인증_정보 = 인증_정보를_가져온다(사용자가_로그인을_시도한다(소피아()));

            // when
            var 실제_응답 = 인증된_사용자가_POST_요청을_보낸다(인증_정보, "/api/v1/user/refresh");

            // then
            assertSoftly(softly -> {
                softly.assertThat(실제_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

                String 갱신된_리프레시_토큰 = 실제_응답.cookie(리프레시_토큰_쿠키_이름);
                softly.assertThat(갱신된_리프레시_토큰).isNotEmpty();

                String 갱신된_액세스_토큰 = 실제_응답.header(HttpHeaders.AUTHORIZATION);
                softly.assertThat(갱신된_액세스_토큰).isNotEmpty();
                softly.assertThat(갱신된_액세스_토큰).isNotEqualTo(인증_정보.accessToken());
            });
        }

        @Test
        void 리프레시_토큰_없이_요청을_보내면_실패한다() {
            // when
            var 실제_응답 = 인증되지_않은_사용자가_POST_요청을_보낸다("/api/v1/user/refresh");

            // then
            assertThat(실제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        void 저장소에_리프레시_토큰이_없으면_실패한다() {
            // given
            var 인증_정보 = 인증_정보를_생성한다(라이언());

            // when
            var 실제_응답 = 인증된_사용자가_POST_요청을_보낸다(인증_정보, "/api/v1/user/refresh");

            // then
            assertThat(실제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private ExtractableResponse<?> 사용자가_로그인을_시도한다(User 사용자) {
        var 일반_로그인_요청 = 로그인_하는_사용자는(사용자).이다();

        return 인증되지_않은_사용자가_POST_요청을_보낸다("/api/v1/user/login", 일반_로그인_요청);
    }
}
