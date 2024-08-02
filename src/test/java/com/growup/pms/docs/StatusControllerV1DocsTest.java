package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.status.controller.dto.request.StatusCreateRequest;
import com.growup.pms.status.controller.dto.response.PageResponse;
import com.growup.pms.status.controller.dto.response.StatusResponse;
import com.growup.pms.status.service.StatusService;
import com.growup.pms.status.service.dto.StatusCreateDto;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.status.StatusCreateRequestTestBuilder;
import com.growup.pms.test.fixture.status.StatusResponseTestBuilder;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import java.util.List;
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
        StatusCreateRequest 상태_생성_요청 = StatusCreateRequestTestBuilder.상태_생성_요청은().이다();
        StatusResponse 예상_상태_응답 = StatusResponseTestBuilder.상태_응답은().이다();

        when(statusService.createStatus(any(StatusCreateDto.class)))
                .thenReturn(예상_상태_응답);

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
                                        fieldWithPath("statusId").type(JsonFieldType.NUMBER)
                                                .description("생성퇸 상태 식별자"),
                                        fieldWithPath("projectId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 식별자"),
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("상태 이름"),
                                        fieldWithPath("colorCode").type(JsonFieldType.STRING)
                                                .description("색상 코드"),
                                        fieldWithPath("sortOrder").type(JsonFieldType.NUMBER)
                                                .description("정렬순서")
                                )
                                .responseHeaders(headerWithName(HttpHeaders.LOCATION).description("생성된 상태의 URL"))
                                .build())));
    }

    @Test
    void 상태_목록조회_API_문서를_생성한다() throws Exception {
        // given
        Long 조회할_프로젝트_ID = 1L;
        StatusResponse statusResponse1 = StatusResponseTestBuilder.상태_응답은().이다();
        StatusResponse statusResponse2 = StatusResponseTestBuilder.상태_응답은()
                .상태_식별자는(2L)
                .이름은("진행중")
                .색상코드는("FFFFF0")
                .정렬순서는((short) 1)
                .이다();

        List<StatusResponse> statusResponses = List.of(statusResponse1, statusResponse2);
        PageResponse<List<StatusResponse>> response = PageResponse.of(false, statusResponses);

        when(statusService.getStatuses(조회할_프로젝트_ID))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/status")
                        .param("projectId", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로젝트 상태 목록 조회")
                                .description("프로젝트 내의 모든 상태를 조회합니다.")
                                .queryParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("조회할 프로젝트 식별자")
                                )
                                .responseFields(
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 존재 여부"),
                                        fieldWithPath("items").type(JsonFieldType.ARRAY)
                                                .description("프로젝트 내에 존재하는 상태 목록"),
                                        fieldWithPath("items[].statusId").type(JsonFieldType.NUMBER)
                                                .description("상태 식별자"),
                                        fieldWithPath("items[].projectId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 식별자"),
                                        fieldWithPath("items[].name").type(JsonFieldType.STRING)
                                                .description("상태 이름"),
                                        fieldWithPath("items[].colorCode").type(JsonFieldType.STRING)
                                                .description("색상 코드"),
                                        fieldWithPath("items[].sortOrder").type(JsonFieldType.NUMBER)
                                                .description("정렬 순서")
                                )
                                .build()
                )));

    }
}
