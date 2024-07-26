package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.growup.pms.status.controller.dto.request.CreateStatusRequest;
import com.growup.pms.status.service.StatusService;
import com.growup.pms.status.service.dto.CreateStatusDto;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.status.CreateStatusRequestTestBuilder;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
public class StatusControllerV1DocsTest extends ControllerSliceTestSupport {

    static final String TAG = "Status";

    @Autowired
    StatusService statusService;

    @Test
    void 상태등록_API_문서를_생성한다() throws Exception {
        // given
        CreateStatusRequest 상태_생성_요청 = CreateStatusRequestTestBuilder.상태_생성_요청은().이다();
        Long 예상_상태_ID = 1L;

        when(statusService.createStatus(any(CreateStatusDto.class)))
                .thenReturn(예상_상태_ID);

        // when & then
        mockMvc.perform(post("/api/v1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(상태_생성_요청)))
                .andExpectAll(
                        status().isCreated())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로젝트 상태 생성")
                                .description("프로젝트의 식별자와 상태의 이름, 색상코드, 정렬 순서를 입력 받습니다.")
                                .requestFields(
                                        fieldWithPath("projectId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 식별자"),
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("상태 이름"),
                                        fieldWithPath("colorCode").type(JsonFieldType.STRING)
                                                .description("색상 코드"),
                                        fieldWithPath("sortOrder").type(JsonFieldType.NUMBER)
                                                .description("정렬순서"))
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE))
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                                .description("생성된 상태 ID")
                                )
                                .build())));
    }
}
