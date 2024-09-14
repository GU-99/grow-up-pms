package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.growup.pms.test.fixture.auth.builder.LoginRequestTestBuilder.로그인_하는_사용자는;
import static com.growup.pms.test.fixture.auth.builder.TokenResponseTestBuilder.발급된_토큰은;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.auth.controller.dto.request.LoginRequest;
import com.growup.pms.auth.service.JwtLoginService;
import com.growup.pms.auth.service.RedisRefreshTokenService;
import com.growup.pms.auth.service.dto.UserLoginCommand;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class LoginControllerV1DocsTest extends ControllerSliceTestSupport {
    static final String TAG = "Auth";

    @Autowired
    JwtLoginService loginService;

    @Autowired
    RedisRefreshTokenService refreshTokenService;

    @Test
    void 로그인_API_문서를_생성한다() throws Exception {
        // given
        LoginRequest 유효한_요청 = 로그인_하는_사용자는().이다();
        TokenResponse 예상하는_발급된_토큰 = 발급된_토큰은().이다();

        when(loginService.authenticateUser(any(UserLoginCommand.class))).thenReturn(예상하는_발급된_토큰);

        // when & then
        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유효한_요청)))
                .andExpectAll(
                        status().isOk(),
                        header().string(HttpHeaders.AUTHORIZATION, "Bearer " + 예상하는_발급된_토큰.accessToken()),
                        cookie().value("refreshToken", 예상하는_발급된_토큰.refreshToken()))
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("일반 로그인")
                                .description("아이디와 비밀번호를 통해 로그인합니다.")
                                .requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"))
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .responseHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).type(SimpleType.STRING).description("액세스 토큰"),
                                        headerWithName("Set-Cookie").type(SimpleType.STRING).description("리프레시 토큰")).build())));
    }

    @Test
    @WithMockSecurityUser(id = 1L)
    void 로그아웃_API_문서를_생성한다() throws Exception {
        // given
        TokenResponse 기존_발급된_토큰 = 발급된_토큰은().리프레시_토큰이("기존 리프레시 토큰").이다();
        doNothing().when(refreshTokenService).revoke(anyLong(), anyString());

        // when & then
        mockMvc.perform(post("/api/v1/user/logout")
                        .cookie(new Cookie("refreshToken", 기존_발급된_토큰.refreshToken())))
                .andExpectAll(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("로그아웃")
                                .description("사용자의 리프레시 토큰을 만료시킵니다. (요청 쿠키로 refreshToken 필요함)").build())));
    }

    @Test
    void 토큰_재발급_API_문서를_생성한다() throws Exception {
        // given
        String 유효한_리프레시_토큰 = "유효한 리프레시 토큰";
        TokenResponse 발급된_새_토큰 = 발급된_토큰은().이다();

        when(refreshTokenService.refreshJwtTokens(유효한_리프레시_토큰)).thenReturn(발급된_새_토큰);

        // when & then
        mockMvc.perform(post("/api/v1/user/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", 유효한_리프레시_토큰)))
                .andExpectAll(
                        status().isOk(),
                        header().string(HttpHeaders.AUTHORIZATION, "Bearer " + 발급된_새_토큰.accessToken()))
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("토큰 재발급")
                                .description("쿠키를 통해 전달된 리프레시 토큰(refreshToken)을 통해 새로운 액세스 토큰을 발급합니다.")
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .responseHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).type(SimpleType.STRING).description("새 액세스 토큰")).build())));
    }
}
