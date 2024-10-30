package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.growup.pms.file.service.ProfileImageService;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class FileControllerV1DocsTest extends ControllerSliceTestSupport {
    static final String TAG = "File";

    @Autowired
    ProfileImageService profileImageService;

    @Test
    void 파일_다운로드_API_문서를_생성한다() throws Exception {
        // given
        String 파일_이름 = "temp_profile_image.png";

        when(profileImageService.download(파일_이름)).thenReturn(더미_이미지를_생성한다(1, 1));

        // when & then
        mockMvc.perform(get("/api/v1/file/profile/{fileName}", 파일_이름)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpectAll(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로필 이미지 다운로드")
                                .description("현재 사용자의 프로필 이미지를 다운로드 합니다.")
                                .requestHeaders(headerWithName(org.apache.http.HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE))
                                .build()
                )));
    }

    @Test
    @WithMockSecurityUser(id = 1L)
    void 프로필_이미지_삭제_API_문서를_생성한다() throws Exception {
        // given
        Long 사용자_ID = 1L;

        doNothing().when(profileImageService).delete(사용자_ID);

        // when & then
        mockMvc.perform(delete("/api/v1/user/profile/image")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로필 이미지 삭제")
                                .description("현재 사용자의 프로필 이미지를 삭제합니다.")
                                .requestHeaders(headerWithName(org.apache.http.HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE))
                                .build()
                )));
    }

    private byte[] 더미_이미지를_생성한다(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(new Color(200, 200, 200));
        graphics.fillRect(0, 0, width, height);

        graphics.dispose();

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create dummy image", e);
        }
    }
}
