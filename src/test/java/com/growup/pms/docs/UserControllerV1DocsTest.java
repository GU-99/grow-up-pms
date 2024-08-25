package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import com.growup.pms.user.service.UserService;
import com.growup.pms.user.service.dto.UserDownloadCommand;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
public class UserControllerV1DocsTest extends ControllerSliceTestSupport {

    static final String TAG = "User";

    @Autowired
    UserService userService;

    @Test
    @WithMockSecurityUser(id = 1L)
    void 프로필_이미지_업로드_API_문서를_생성한다() throws  Exception {
        // Given: Mock 설정
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
            mockMvc.perform(
                    multipart("/api/v1/users/file")
                        .file(업로드되는_파일)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                        .andExpect(status().isOk())
                        .andDo(
                            docs.document(
                                    requestParts(
                                            partWithName("file").description("업로드할 파일")
                                    )
                        )
                    );
        }
    }

    @Test
    @WithMockSecurityUser(id = 1L)
    void 프로필_이미지_다운로드_API_문서를_생성한다() throws  Exception {
        // Given: Mock 설정
        Path 다운로드할_파일_경로 = Path.of("src/test/resources/images/testImage.jpg");
        String 다운로드할_파일_이름 = "download.jpg";
        byte[] 다운로드한_파일의_Bytes = Files.readAllBytes(다운로드할_파일_경로);
        UserDownloadCommand 다운로드할_파일_정보 = new UserDownloadCommand(다운로드할_파일_이름, new UrlResource(다운로드할_파일_경로.toUri()));

        when(userService.imageDownload(anyLong())).thenReturn(다운로드할_파일_정보);

        // when & then
        mockMvc.perform(
                        get("/api/v1/users/file")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                ).andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + 다운로드할_파일_이름+ "\""))
                .andExpect(content().bytes(다운로드한_파일의_Bytes))
                .andDo(
                        docs.document(resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG)
                                        .summary("사용자 프로필 다운로드")
                                        .description("사용자의 프로필을 다운로드합니다.")
                                        .requestHeaders(
                                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 액세스 토큰")
                                        )
                                        .responseHeaders(
                                                headerWithName(HttpHeaders.CONTENT_DISPOSITION).description("attachment; filename=\"" + 다운로드할_파일_이름+ "\"")
                                        )
                                        .build()
                        ))
                );
    }

}
