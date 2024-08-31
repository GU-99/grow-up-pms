package com.growup.pms.test.support.rest;

import com.growup.pms.common.security.jwt.dto.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationAcceptanceTestAction {
    public static final String 액세스_토큰_접두사 = "Bearer ";
    public static final String 리프레시_토큰_쿠키_이름 = "refreshToken";

    public static ExtractableResponse<?> 인증되지_않은_사용자가_POST_요청을_보낸다(String 엔드포인트) {
        return RestAssured
                .given().log().all()
                .when().post(엔드포인트)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<?> 인증되지_않은_사용자가_POST_요청을_보낸다(String 엔드포인트, Object 요청_본문) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(요청_본문)
                .when().post(엔드포인트)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<?> 인증된_사용자가_POST_요청을_보낸다(TokenResponse 인증_정보, String 엔드포인트) {
        return RestAssured
                .given().log().all()
                .cookie(리프레시_토큰_쿠키_이름, 인증_정보.refreshToken())
                .headers(HttpHeaders.AUTHORIZATION, 액세스_토큰_접두사 + 인증_정보.accessToken())
                .when().post(엔드포인트)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<?> 인증된_사용자가_POST_요청을_보낸다(TokenResponse 인증_정보, String 엔드포인트, Object 요청_본문) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(리프레시_토큰_쿠키_이름, 인증_정보.refreshToken())
                .headers(HttpHeaders.AUTHORIZATION, 액세스_토큰_접두사 + 인증_정보.accessToken())
                .body(요청_본문)
                .when().post(엔드포인트)
                .then().log().all()
                .extract();
    }

    public static TokenResponse 인증_정보를_가져온다(ExtractableResponse<?> 실제_응답) {
        String 액세스_토큰 = 실제_응답.header(HttpHeaders.AUTHORIZATION).substring(액세스_토큰_접두사.length());
        String 리프레시_토큰 = 실제_응답.cookie(리프레시_토큰_쿠키_이름);
        return new TokenResponse(액세스_토큰, 리프레시_토큰);
    }
}
