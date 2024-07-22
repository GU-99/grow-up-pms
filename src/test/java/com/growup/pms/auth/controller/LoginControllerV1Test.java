package com.growup.pms.auth.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.growup.pms.auth.dto.LoginRequest;
import com.growup.pms.auth.service.JwtLoginService;
import com.growup.pms.auth.service.JwtTokenService;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.common.security.jwt.dto.TokenDto;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.auth.LoginRequestFixture;
import com.growup.pms.test.fixture.auth.TokenDtoFixture;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class LoginControllerV1Test extends ControllerSliceTestSupport {
    static final String TAG = "Auth";

    @Autowired
    JwtLoginService loginService;

    @Autowired
    JwtTokenService tokenService;

    @Nested
    class 사용자가_로그인_시에 {

        @Test
        void 성공한다() throws Exception {
            // given
            LoginRequest validRequest = LoginRequestFixture.createDefaultRequest();
            TokenDto expectedValidToken = TokenDtoFixture.createDefaultDtoBuilder()
                    .accessToken(TokenDtoFixture.VALID_ACCESS_TOKEN)
                    .refreshToken(TokenDtoFixture.VALID_REFRESH_TOKEN)
                    .build();

            when(loginService.authenticateUser(any(LoginRequest.class))).thenReturn(expectedValidToken);

            // when & then
            mockMvc.perform(post("/api/v1/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpectAll(
                            status().isOk(),
                            header().string(HttpHeaders.AUTHORIZATION, "Bearer " + TokenDtoFixture.VALID_ACCESS_TOKEN),
                            cookie().value("refreshToken", TokenDtoFixture.VALID_REFRESH_TOKEN))
                    .andDo(docs.document(resource(
                            ResourceSnippetParameters.builder()
                                    .tag(TAG)
                                    .summary("일반 로그인")
                                    .description("아이디와 비밀번호를 통해 로그인합니다.")
                                    .requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호"))
                                    .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                    .responseHeaders(
                                            headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰"),
                                            headerWithName("Set-Cookie").description("리프레시 토큰")).build())));
        }

        @Test
        void 매치되는_정보가_없으면_예외가_발생한다() throws Exception {
            // given
            LoginRequest badRequest = LoginRequestFixture.createDefaultRequest();

            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)).when(loginService).authenticateUser(any(
                    LoginRequest.class));

            // when & then
            mockMvc.perform(post("/api/v1/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badRequest)))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.accessToken").doesNotExist(),
                            cookie().doesNotExist("refreshToken"));
        }
    }

    @Nested
    class 사용자가_토큰_재발급_시에 {

        @Test
        void 성공한다() throws Exception {
            // given
            String validRefreshToken = TokenDtoFixture.VALID_REFRESH_TOKEN;
            TokenDto newTokens = TokenDtoFixture.createDefaultDto();

            when(tokenService.refreshJwtTokens(validRefreshToken)).thenReturn(newTokens);

            // when & then
            mockMvc.perform(post("/api/v1/user/login/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .cookie(new Cookie("refreshToken", validRefreshToken)))
                    .andExpectAll(
                            status().isOk(),
                            header().string(HttpHeaders.AUTHORIZATION, "Bearer " + newTokens.getAccessToken()),
                            cookie().value("refreshToken", newTokens.getRefreshToken()))
                    .andDo(docs.document(resource(
                            ResourceSnippetParameters.builder()
                                    .tag(TAG)
                                    .summary("토큰 재발급")
                                    .description("쿠키를 통해 전달된 리프레시 토큰(refreshToken)을 통해 새로운 토큰을 발급합니다.")
                                    .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                    .responseHeaders(
                                            headerWithName(HttpHeaders.AUTHORIZATION).description("새 액세스 토큰"),
                                            headerWithName(HttpHeaders.SET_COOKIE).description("새 리프레시 토큰")).build())));
        }

        @Test
        void 리프레시_토큰이_유효하지_않으면_오류코드를_반환한다() throws Exception {
            // given
            doThrow(new AuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN_ERROR)).when(tokenService).refreshJwtTokens(any(String.class));

            // when & then
            mockMvc.perform(post("/api/v1/user/login/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", TokenDtoFixture.INVALID_REFRESH_TOKEN)))
                    .andExpectAll(
                            status().isUnauthorized(),
                            jsonPath("$.accessToken").doesNotExist());
        }
    }
}
