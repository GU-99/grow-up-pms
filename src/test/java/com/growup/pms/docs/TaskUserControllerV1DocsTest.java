package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static com.growup.pms.test.fixture.task.builder.TaskUserCreateRequestTestBuilder.일정_수행자_추가_요청은;
import static com.growup.pms.test.fixture.task.builder.TaskUserResponseTestBuilder.일정_수행자_목록_응답은;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.role.domain.ProjectRole;
import com.growup.pms.task.controller.dto.request.TaskUserCreateRequest;
import com.growup.pms.task.controller.dto.response.TaskUserResponse;
import com.growup.pms.task.service.TaskUserService;
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
                                .description("회원 식별자를 입력 받습니다.")
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

    @Test
    @WithMockSecurityUser
    void 일정_수행자_목록조회_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_프로젝트_식별자 = 1L;
        Long 예상_일정_식별자 = 1L;
        TaskUserResponse 예상_결과_항목_1 = 일정_수행자_목록_응답은()
                .회원_식별자가(1L)
                .닉네임이("브라운")
                .역할_이름이(ProjectRole.ADMIN.getRoleName())
                .이다();
        TaskUserResponse 예상_결과_항목_2 = 일정_수행자_목록_응답은()
                .회원_식별자가(2L)
                .닉네임이("코니")
                .역할_이름이(ProjectRole.LEADER.getRoleName())
                .이다();
        List<TaskUserResponse> 예상_결과 = List.of(예상_결과_항목_1, 예상_결과_항목_2);

        // when
        when(taskUserService.getAssignees(anyLong(), anyLong())).thenReturn(예상_결과);

        // then
        mockMvc.perform(get("/api/v1/project/{projectId}/task/{taskId}/assignee", 예상_프로젝트_식별자, 예상_일정_식별자)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpect(
                        status().isOk()
                )
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .requestSchema(schema("프로젝트 일정 수행자 목록 조회 예시입니다."))
                                .summary("프로젝트 일정 수행자 목록 조회")
                                .description("프로젝트와 일정의 식별자를 사용해서 해당 프로젝트 일정 수행자 목록을 조회합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("프로젝트 식별자"),
                                        parameterWithName("taskId").type(SimpleType.NUMBER)
                                                .description("프로젝트 일정 식별자")
                                )
                                .responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                .description("프로젝트 일정 수행자 목록"),
                                        fieldWithPath("[].userId").type(JsonFieldType.NUMBER)
                                                .description("회원 식별자"),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING)
                                                .description("회원 닉네임"),
                                        fieldWithPath("[].roleName").type(JsonFieldType.STRING)
                                                .description("프로젝트 내 회원 역할")
                                )
                                .build())));
    }
}
