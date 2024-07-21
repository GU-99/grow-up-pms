package com.growup.pms.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.growup.pms.auth.dto.LoginRequest;
import com.growup.pms.common.security.jwt.dto.TokenDto;
import com.growup.pms.auth.service.JwtLoginService;
import com.growup.pms.auth.service.JwtTokenService;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.test.support.DefaultControllerSliceTest;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.auth.LoginRequestFixture;
import com.growup.pms.test.fixture.auth.TokenDtoFixture;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class LoginControllerV1Test extends DefaultControllerSliceTest {
    @Autowired
    private JwtLoginService loginService;

    @Autowired
    private JwtTokenService tokenService;

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
                            cookie().value("refreshToken", TokenDtoFixture.VALID_REFRESH_TOKEN)
                    );
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
                            cookie().doesNotExist("refreshToken")
                    );
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
                            cookie().value("refreshToken", newTokens.getRefreshToken())
                    );
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
                            jsonPath("$.accessToken").doesNotExist()
                    );
        }
    }
}
