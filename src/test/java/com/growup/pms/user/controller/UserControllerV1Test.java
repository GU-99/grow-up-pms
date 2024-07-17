package com.growup.pms.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.growup.pms.test.DefaultControllerSliceTest;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.user.UserCreateRequestFixture;
import com.growup.pms.user.dto.UserCreateRequest;
import com.growup.pms.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class UserControllerV1Test extends DefaultControllerSliceTest {
    @Autowired
    UserService userService;

    @Test
    void 사용자가_계정을_생성한다() throws Exception {
        // given
        Long userId = 1L;
        UserCreateRequest request = UserCreateRequestFixture.createDefaultRequest();

        when(userService.save(any(UserCreateRequest.class))).thenReturn(userId);

        // when & then
        mockMvc.perform(post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/v1/users/" + userId)
                );
    }
}
