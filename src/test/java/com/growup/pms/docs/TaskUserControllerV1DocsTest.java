package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static com.growup.pms.test.fixture.task.builder.TaskUserCreateRequestTestBuilder.일정_수행자_추가_요청은;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.task.controller.dto.request.TaskUserCreateRequest;
import com.growup.pms.task.service.TaskUserService;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
public class TaskUserControllerV1DocsTest extends ControllerSliceTestSupport {

    static final String TAG = "TaskUser";

    @Autowired
    TaskUserService taskUserService;

    @Test
    @WithMockSecurityUser
    void 일정_수행자_추가_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_프로젝트_식별자 = 1L;
        Long 예상_일정_식별자 = 1L;
        TaskUserCreateRequest 일정_수행자_추가_요청 = 일정_수행자_추가_요청은().이다();

        // when
        doNothing().when(taskUserService).createTaskUser(anyLong(), anyLong());

        // then
        mockMvc.perform(post("/api/v1/project/{projectId}/task/{taskId}/assignee", 예상_프로젝트_식별자, 예상_일정_식별자)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(일정_수행자_추가_요청))
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpect(
                        status().isOk()
                )
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .requestSchema(schema("프로젝트 일정 수행자 추가 요청 예시입니다."))
                                .summary("프로젝트 일정 생성")
                                .description("프로젝트의 상태 식별자와 일정 이름, 일정 내용, 정렬 순서, 시작일자, 종료일자를 입력 받습니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("프로젝트 식별자"),
                                        parameterWithName("taskId").type(SimpleType.NUMBER)
                                                .description("프로젝트 일정 식별자")
                                )
                                .requestFields(
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                                .description("회원 식별자")
                                )
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE))
                                .build())));
    }
}
