package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static com.growup.pms.test.fixture.project.builder.ProjectRoleEditRequestTestBuilder.프로젝트원_역할_변경_요청은;
import static com.growup.pms.test.fixture.project.builder.ProjectUserCreateRequestTestBuilder.프로젝트_유저_생성_요청은;
import static com.growup.pms.test.fixture.project.builder.ProjectUserResponseTestBuilder.프로젝트원은;
import static com.growup.pms.test.fixture.project.builder.ProjectUserSearchResponseTestBuilder.검색된_프로젝트원은;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.project.controller.dto.request.ProjectRoleEditRequest;
import com.growup.pms.project.controller.dto.request.ProjectUserCreateRequest;
import com.growup.pms.project.controller.dto.response.ProjectUserResponse;
import com.growup.pms.project.controller.dto.response.ProjectUserSearchResponse;
import com.growup.pms.project.service.ProjectUserService;
import com.growup.pms.project.service.dto.ProjectUserCreateCommand;
import com.growup.pms.role.domain.ProjectRole;
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
    @WithMockSecurityUser
    void 프로젝트원_목록조회_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_프로젝트_ID = 1L;
        ProjectUserResponse 예상_프로젝트원_1 = 프로젝트원은()
                .회원_식별자가(1L)
                .닉네임이("브라운")
                .역할이름이(ProjectRole.ADMIN.getRoleName())
                .이다();

        ProjectUserResponse 예상_프로젝트원_2 = 프로젝트원은()
                .회원_식별자가(2L)
                .닉네임이("레니")
                .역할이름이(ProjectRole.LEADER.getRoleName())
                .이다();

        ProjectUserResponse 예상_프로젝트원_3 = 프로젝트원은()
                .회원_식별자가(3L)
                .닉네임이("레너드")
                .역할이름이(ProjectRole.ASSIGNEE.getRoleName())
                .이다();
        List<ProjectUserResponse> 예상_결과 = List.of(예상_프로젝트원_1, 예상_프로젝트원_2, 예상_프로젝트원_3);

        // when
        when(projectUserService.getProjectUsers(anyLong())).thenReturn(예상_결과);

        // then
        mockMvc.perform(get("/api/v1/project/{projectId}/user", 예상_프로젝트_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰")
                )
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .requestSchema(schema("프로젝트원 목록조회 요청 예시 입니다."))
                                .summary("프로젝트원 목록 조회")
                                .description("프로젝트에 속한 프로젝트원의 목록을 조회합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("프로젝트 ID")
                                )
                                .requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description(MediaType.APPLICATION_JSON_VALUE)
                                )
                                .responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                .description("프로젝트원 목록"),
                                        fieldWithPath("[].userId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트원의 회원 ID"),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING)
                                                .description("프로젝트원의 회원 닉네임"),
                                        fieldWithPath("[].roleName").type(JsonFieldType.STRING)
                                                .description("프로젝트원의 프로젝트 내 권한")
                                )
                                .build())));
    }

    @Test
    @WithMockSecurityUser
    void 프로젝트원_검색_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_프로젝트_ID = 1L;
        String prefix = "레";
        ProjectUserSearchResponse response2 = 검색된_프로젝트원은()
                .회원_식별자가(1L)
                .닉네임이("레니")
                .이다();
        ProjectUserSearchResponse response3 = 검색된_프로젝트원은()
                .회원_식별자가(2L)
                .닉네임이("레너드")
                .이다();

        List<ProjectUserSearchResponse> responses = List.of(response2, response3);

        // when
        when(projectUserService.searchProjectUsersByPrefix(anyLong(), anyString())).thenReturn(responses);

        // then
        mockMvc.perform(get("/api/v1/project/{projectId}/user/search", 예상_프로젝트_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰")
                        .queryParam("nickname", prefix)
                )
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .requestSchema(schema("프로젝트원 검색 요청 예시 입니다."))
                                .summary("닉네임으로 프로젝트원 검색")
                                .description("접두사로 시작하는 닉네임을 가진 프로젝트원의 목록을 조회합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("프로젝트 ID")
                                )
                                .queryParameters(
                                        parameterWithName("nickname").type(SimpleType.STRING)
                                                .description("검색어")
                                )
                                .requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description(MediaType.APPLICATION_JSON_VALUE)
                                )
                                .responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                .description("프로젝트원 목록"),
                                        fieldWithPath("[].userId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트원의 회원 ID"),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING)
                                                .description("프로젝트원의 회원 닉네임")
                                )
                                .build())));
    }

    @Test
    void 프로젝트원_역할_변경_API_문서를_생성한다() throws Exception {
        // given
        Long 프로젝트_ID = 1L;
        Long 변경할_팀원_ID = 1L;
        ProjectRoleEditRequest 프로젝트_역할_변경_요청 = 프로젝트원_역할_변경_요청은().이다();

        // when
        doNothing().when(projectUserService).changeRole(프로젝트_ID, 변경할_팀원_ID, 프로젝트_역할_변경_요청.roleName());

        // then
        mockMvc.perform(patch("/api/v1/project/{projectId}/user/{userId}/role", 프로젝트_ID, 변경할_팀원_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(프로젝트_역할_변경_요청))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰")
                )
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .requestSchema(schema("프로젝트원 역할 변경 요청 예시 입니다."))
                                .summary("프로젝트원 역할 변경")
                                .description("프로젝트 ID, 프로젝트원의 회원 ID, 변경할 역할 이름을 통해 프로젝트원의 역할을 변경합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("프로젝트 ID"),
                                        parameterWithName("userId").type(SimpleType.NUMBER)
                                                .description("변경할 회원 ID")
                                )
                                .requestFields(
                                        fieldWithPath("roleName").type(JsonFieldType.STRING)
                                                .description("변경할 프로젝트 역할 이름")
                                )
                                .build()
                )));
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
                                .requestSchema(schema("프로젝트원 제거 요청 예시 입니다."))
                                .summary("프로젝트원 제거")
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
