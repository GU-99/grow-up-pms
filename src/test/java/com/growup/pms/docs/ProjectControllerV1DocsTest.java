package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static com.growup.pms.test.fixture.project.builder.ProjectCreateRequestTestBuilder.프로젝트_생성_요청은;
import static com.growup.pms.test.fixture.project.builder.ProjectResponseTestBuilder.프로젝트_목록조회_응답은;
import static com.growup.pms.test.fixture.project.builder.ProjectUserCreateRequestTestBuilder.프로젝트_유저_생성_요청은;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growup.pms.project.controller.dto.request.ProjectCreateRequest;
import com.growup.pms.project.controller.dto.request.ProjectUserCreateRequest;
import com.growup.pms.project.controller.dto.response.ProjectResponse;
import com.growup.pms.project.service.ProjectService;
import com.growup.pms.project.service.dto.ProjectCreateCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
public class ProjectControllerV1DocsTest extends ControllerSliceTestSupport {

    static final String TAG = "Project";

    @Autowired
    ProjectService projectService;

    @Test
    @WithMockSecurityUser
    void 프로젝트_생성_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_팀_ID = 1L;
        List<ProjectUserCreateRequest> 초대할_팀원_목록 = List.of(프로젝트_유저_생성_요청은().이다());
        ProjectCreateRequest 프로젝트_생성_요청 = 프로젝트_생성_요청은()
                .초대할_팀원들은(초대할_팀원_목록)
                .이다();
        Long 예상_프로젝트_ID = 1L;

        // when
        when(projectService.createProject(anyLong(), anyLong(), any(ProjectCreateCommand.class)))
                .thenReturn(예상_프로젝트_ID);

        objectMapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // then
        mockMvc.perform(post("/api/v1/team/{teamId}/project", 예상_팀_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(프로젝트_생성_요청))
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/v1/team/1/project/1")
                )
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .requestSchema(schema("프로젝트 생성 요청 예시입니다."))
                                .summary("프로젝트 생성")
                                .description("프로젝트의 이름, 설명, 시작일자, 종료일자를 입력 받습니다.")
                                .pathParameters(
                                        parameterWithName("teamId").type(SimpleType.NUMBER)
                                                .description("팀 ID")
                                )
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description(MediaType.APPLICATION_JSON_VALUE))
                                .requestFields(
                                        fieldWithPath("projectName").type(JsonFieldType.STRING)
                                                .description("프로젝트 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("프로젝트 설명"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("프로젝트 시작 일자"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("프로젝트 종료 일자"),
                                        fieldWithPath("coworkers").type(JsonFieldType.ARRAY)
                                                .description("프로젝트에 초대할 팀원 목록"),
                                        fieldWithPath("coworkers[].userId").type(JsonFieldType.NUMBER)
                                                .description("초대할 팀원의 회원 ID"),
                                        fieldWithPath("coworkers[].roleName").type(JsonFieldType.STRING)
                                                .description("초대할 팀원의 프로젝트 내에서의 권한")
                                )
                                .responseHeaders(
                                        headerWithName(org.springframework.http.HttpHeaders.LOCATION)
                                                .description("생성된 프로젝트 URL")
                                )
                                .build())));
    }

    @Test
    void 프로젝트_목록조회_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_팀_ID = 1L;
        ProjectResponse 예상_프로젝트_1 = 프로젝트_목록조회_응답은().프로젝트_식별자는(1L).이다();
        ProjectResponse 예상_프로젝트_2 = 프로젝트_목록조회_응답은().프로젝트_식별자는(2L).이다();
        ProjectResponse 예상_프로젝트_3 = 프로젝트_목록조회_응답은().프로젝트_식별자는(3L).이다();
        List<ProjectResponse> 예상_결과 = List.of(예상_프로젝트_1, 예상_프로젝트_2, 예상_프로젝트_3);

        // when
        when(projectService.getProjects(anyLong())).thenReturn(예상_결과);

        // then
        mockMvc.perform(get("/api/v1/team/{teamId}/project", 예상_팀_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로젝트 목록조회")
                                .description("팀 내에 존재하는 모든 프로젝트의 목록을 조회합니다.")
                                .pathParameters(
                                        parameterWithName("teamId").type(SimpleType.NUMBER)
                                                .description("팀 ID")
                                )
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description(MediaType.APPLICATION_JSON_VALUE))
                                .responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                .description("팀 ID에 해당하는 프로젝트 목록"),
                                        fieldWithPath("[].projectId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 ID"),
                                        fieldWithPath("[].projectName").type(JsonFieldType.STRING)
                                                .description("프로젝트 이름"),
                                        fieldWithPath("[].content").type(JsonFieldType.STRING)
                                                .description("프로젝트 설명"),
                                        fieldWithPath("[].startDate").type(JsonFieldType.STRING)
                                                .description("프로젝트 시작 일자"),
                                        fieldWithPath("[].endDate").type(JsonFieldType.STRING)
                                                .description("프로젝트 종료 일자"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                                .description("프로젝트 생성일시"),
                                        fieldWithPath("[].updatedAt").type(JsonFieldType.STRING)
                                                .description("프로젝트 변경일시")
                                )
                                .build())));
    }
}
