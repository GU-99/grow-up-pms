package com.growup.pms.common.exception.handler;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WithUserDetails
@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(GlobalExceptionHandlerTest.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @TestConfiguration
    public static class TestConfig {

        @RestController
        @RequestMapping("/test")
        public static class TestController {

            @GetMapping("/entity")
            public String testEntityNotFoundException() {
                throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND);
            }

            @GetMapping("/auth")
            public String testAuthenticationException() {
                throw new AuthenticationException(ErrorCode.AUTH_AUTHENTICATION_ERROR);
            }

            @GetMapping("/authz")
            public String testAuthorizationException() {
                throw new AuthorizationException(ErrorCode.AUTHZ_ACCESS_DENIED);
            }

            @GetMapping("/internal")
            public String testInternalServerException() throws Exception {
                throw new Exception();
            }
        }
    }

    @Test
    void EntityNotFoundException_테스트를_한다() throws Exception {
        // given
        String url = "/test/entity";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Entity error: ENTITY가 없습니다."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("ET_001"));
    }

    @Test
    void AuthenticationException_테스트를_한다() throws Exception {
        // given
        String url = "/test/auth";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Authentication error: 인증을 실패했습니다."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("AT_001"));
    }

    @Test
    void AuthorizationException_테스트를_한다() throws Exception {
        // given
        String url = "/test/authz";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(status().isForbidden())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Authorization error: 접근 권한이 없습니다."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("ATZ_001"));
    }

    @Test
    void InternalServerError_테스트를_한다() throws Exception {
        // given
        String url = "/test/internal";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(status().isInternalServerError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Internal server error: 예상치 못한 서버 에러가 발생했습니다."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("IS_001"));
    }
}
