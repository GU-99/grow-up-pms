package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static com.growup.pms.test.fixture.project.builder.ProjectUserCreateRequestTestBuilder.프로젝트_유저_생성_요청은;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.project.controller.dto.request.ProjectUserCreateRequest;
import com.growup.pms.project.service.ProjectUserService;
import com.growup.pms.project.service.dto.ProjectUserCreateCommand;
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
public class ProjectUserControllerV1DocsTest extends ControllerSliceTestSupport {

    static final String TAG = "ProjectUser";

    @Autowired
    ProjectUserService projectUserService;

    @Test
    @WithMockSecurityUser
    void 프로젝트원_추가_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_프로젝트_ID = 1L;
        ProjectUserCreateRequest 프로젝트원_추가_요청 = 프로젝트_유저_생성_요청은().이다();

        // when
        doNothing().when(projectUserService).createProjectUser(anyLong(), any(ProjectUserCreateCommand.class));

        // then
        mockMvc.perform(post("/api/v1/project/{projectId}/user", 예상_프로젝트_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(프로젝트원_추가_요청))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰")
                )
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .requestSchema(schema("프로젝트원 추가 요청 예시 입니다."))
                                .summary("프로젝트원 추가")
                                .description("프로젝트 ID, 프로젝트원의 회원 ID, 프로젝트원 권한을 통해 프로젝트원을 추가합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("프로젝트 ID")
                                )
                                .requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description(MediaType.APPLICATION_JSON_VALUE)
                                )
                                .requestFields(
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트원의 회원 ID"),
                                        fieldWithPath("roleName").type(JsonFieldType.STRING)
                                                .description("프로젝트원의 프로젝트 내 권한")
                                )
                                .build())));
    }

    @Test
    void 프로젝트원_제거_API_문서를_생성한다() throws Exception {
        // given
        Long 프로젝트_ID = 1L;
        Long 제거할_팀원_ID = 1L;

        // when
        doNothing().when(projectUserService).kickProjectUser(프로젝트_ID, 제거할_팀원_ID);

        // then
        mockMvc.perform(delete("/api/v1/project/{projectId}/user/{userId}", 프로젝트_ID, 제거할_팀원_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰")
                )
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .requestSchema(schema("프로젝트원 삭제 요청 예시 입니다."))
                                .summary("프로젝트원 추가")
                                .description("프로젝트 ID, 프로젝트원의 회원 ID를 통해 프로젝트원을 제거합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("프로젝트 ID"),
                                        parameterWithName("userId").type(SimpleType.NUMBER)
                                                .description("제거할 회원 ID")
                                )
                                .build())));
    }
}
