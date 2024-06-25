package com.growup.pms.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.growup.pms.auth.dto.SignInRequest;
import com.growup.pms.auth.dto.TokenDto;
import com.growup.pms.auth.service.JwtLoginService;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.test.CommonControllerSliceTest;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.auth.SignInRequestFixture;
import com.growup.pms.test.fixture.auth.TokenDtoFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationControllerV1Test extends CommonControllerSliceTest {
    @Autowired
    private JwtLoginService loginService;

    @Nested
    class 사용자가_로그인_시에 {

        @Test
        void 성공한다() throws Exception {
            // given
            SignInRequest validRequest = SignInRequestFixture.createDefaultRequest();
            TokenDto expectedValidToken = TokenDtoFixture.createDefaultDtoBuilder()
                    .accessToken(TokenDtoFixture.VALID_ACCESS_TOKEN)
                    .refreshToken(TokenDtoFixture.VALID_REFRESH_TOKEN)
                    .build();

            when(loginService.authenticateUser(any(SignInRequest.class))).thenReturn(expectedValidToken);

            // when & then
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.access_token").value(TokenDtoFixture.VALID_ACCESS_TOKEN))
                    .andExpect(cookie().value("refresh_token", TokenDtoFixture.VALID_REFRESH_TOKEN));
        }

        @Test
        void 매치되는_정보가_없으면_예외가_발생한다() throws Exception {
            // given
            SignInRequest badRequest = SignInRequestFixture.createDefaultRequest();

            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)).when(loginService).authenticateUser(any(SignInRequest.class));

            // when & then
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.access_token").doesNotExist())
                    .andExpect(cookie().doesNotExist("refresh_token"));
        }
    }
}
