package com.growup.pms.user.controller;

import static com.growup.pms.test.fixture.user.builder.UserCreateRequestTestBuilder.가입하는_사용자는;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import com.growup.pms.user.controller.dto.request.UserCreateRequest;
import com.growup.pms.user.service.UserService;
import com.growup.pms.user.service.dto.UserCreateCommand;
import com.growup.pms.user.service.dto.UserDownloadCommand;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
    class 사용자가_프로필_이미지를_다운로드_시에 {
        @Test
        @WithMockSecurityUser(id = 1L)
        void 성공한다() throws Exception {
            // Given: Mock 설정
            Path 다운로드할_파일_경로 = Path.of("src/test/resources/images/testImage.jpg");
            String 다운로드할_파일_이름 = "download.jpg";
            byte[] 다운로드한_파일의_Bytes = Files.readAllBytes(다운로드할_파일_경로);
            UserDownloadCommand 다운로드할_파일_정보 = new UserDownloadCommand(다운로드할_파일_이름, new UrlResource(다운로드할_파일_경로.toUri()));

            when(userService.imageDownload(anyLong())).thenReturn(다운로드할_파일_정보);

            // when & then
            mockMvc.perform(
                    get("/api/v1/user/file")
                ).andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + 다운로드할_파일_이름+ "\""))
                .andExpect(content().bytes(다운로드한_파일의_Bytes));
        }
    }
}
