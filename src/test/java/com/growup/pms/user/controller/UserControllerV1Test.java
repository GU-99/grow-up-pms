package com.growup.pms.user.controller;

import static com.growup.pms.test.fixture.user.UserCreateRequestTestBuilder.가입하는_사용자는;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import com.growup.pms.user.controller.dto.request.UserCreateRequest;
import com.growup.pms.user.service.UserService;
import com.growup.pms.user.service.dto.UserCreateCommand;
import java.io.FileInputStream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class UserControllerV1Test extends ControllerSliceTestSupport {
    @Autowired
    UserService userService;

    @Nested
    class 사용자가_회원가입_시에 {
        @Test
        void 성공한다() throws Exception {
            // given
            Long 새_사용자_ID = 1L;
            UserCreateRequest 사용자_생성_요청 = 가입하는_사용자는().이다();

            when(userService.save(any(UserCreateCommand.class))).thenReturn(새_사용자_ID);

            // when & then
            mockMvc.perform(post("/api/v1/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(사용자_생성_요청)))
                    .andExpectAll(
                            status().isCreated(),
                            header().string(HttpHeaders.LOCATION, "/api/v1/user/" + 새_사용자_ID));
        }
    }

    @Nested
    class 사용자가_프로필_이미지를_업로드_시에 {
        @Test
        @WithMockSecurityUser(id = 1L)
        void 성공한다() throws Exception {
            // given
            final String 루트_경로 = "src/test/resources/images";
            final String 업로드하는_파일_이름 = "testImage.jpg";

            try (FileInputStream 파일_입력_스트림 = new FileInputStream(루트_경로 + "/" + 업로드하는_파일_이름)) {
                MockMultipartFile 업로드되는_파일 = new MockMultipartFile(
                        "file",
                        업로드하는_파일_이름,
                        MediaType.IMAGE_JPEG_VALUE,
                        파일_입력_스트림
                );

                // when & then
                mockMvc.perform(multipart("/api/v1/user/file")
                                .file(업로드되는_파일)
                ).andExpect(status().isOk());
            }
        }
    }
}
