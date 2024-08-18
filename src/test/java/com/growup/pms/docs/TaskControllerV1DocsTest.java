package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growup.pms.task.controller.dto.request.TaskCreateRequest;
import com.growup.pms.task.controller.dto.request.TaskEditRequest;
import com.growup.pms.task.controller.dto.response.TaskDetailResponse;
import com.growup.pms.task.controller.dto.response.TaskResponse;
import com.growup.pms.task.service.TaskService;
import com.growup.pms.task.service.dto.TaskCreateCommand;
import com.growup.pms.task.service.dto.TaskEditCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.fixture.task.TaskCreateRequestTestBuilder;
import com.growup.pms.test.fixture.task.TaskDetailResponseTestBuilder;
import com.growup.pms.test.fixture.task.TaskEditRequestTestBuilder;
import com.growup.pms.test.fixture.task.TaskResponseTestBuilder;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
public class TaskControllerV1DocsTest extends ControllerSliceTestSupport {

    static final String TAG = "Task";

    @Autowired
    TaskService taskService;

    @Test
    @WithMockSecurityUser
    void 일정등록_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_프로젝트_식별자 = 1L;
        TaskCreateRequest 일정_생성_요청 = TaskCreateRequestTestBuilder.일정_생성_요청은().이다();
        TaskDetailResponse 예상_일정_응답 = TaskDetailResponseTestBuilder.일정_상세조회_응답은().이다();

        // when
        when(taskService.createTask(anyLong(), any(TaskCreateCommand.class)))
                .thenReturn(예상_일정_응답);

        objectMapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // then
        mockMvc.perform(post("/api/v1/project/{projectId}/task", 예상_프로젝트_식별자)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(일정_생성_요청))
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/v1/project/1/task/1")
                )
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로젝트 일정 생성")
                                .description("프로젝트의 상태 식별자와 일정 이름, 일정 내용, 정렬 순서, 시작일자, 종료일자를 입력 받습니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("프로젝트 식별자")
                                )
                                .requestFields(
                                        fieldWithPath("statusId").type(JsonFieldType.NUMBER)
                                                .optional()
                                                .description("프로젝트 상태 식별자"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                                .optional()
                                                .description("담당할 팀원의 회원 식별자"),
                                        fieldWithPath("taskName").type(JsonFieldType.STRING)
                                                .description("일정 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("일정 내용"),
                                        fieldWithPath("sortOrder").type(JsonFieldType.NUMBER)
                                                .description("정렬 순서"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("시작일자"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("종료일자")
                                )
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE))
                                .responseFields(
                                        fieldWithPath("taskId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 일정 식별자"),
                                        fieldWithPath("statusId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 상태 식별자"),
                                        fieldWithPath("userNickname").type(JsonFieldType.STRING)
                                                .description("회원 이름"),
                                        fieldWithPath("taskName").type(JsonFieldType.STRING)
                                                .description("일정 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("일정 내용"),
                                        fieldWithPath("sortOrder").type(JsonFieldType.NUMBER)
                                                .description("정렬 순서"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("시작일자"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("종료일자")
                                )
                                .responseHeaders(
                                        headerWithName(org.springframework.http.HttpHeaders.LOCATION).description(
                                                "생성된 일정의 URL"))
                                .build())));
    }

    @Test
    void 일정_전체조회_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_프로젝트_식별자 = 1L;
        Long 예상_상태_식별자 = 1L;
        TaskResponse response1 = TaskResponseTestBuilder.일정_전체조회_응답은().이다();
        TaskResponse response2 = TaskResponseTestBuilder.일정_전체조회_응답은()
                .일정_식별자는(2L)
                .일정이름은("프로젝트 일정 등록 기능 구현")
                .회원_닉네임은("Hello2")
                .이다();

        List<TaskResponse> responses = List.of(response1, response2);

        // when
        when(taskService.getTasks(anyLong())).thenReturn(responses);

        // then
        mockMvc.perform(get(("/api/v1/project/{projectId}/task"), 예상_프로젝트_식별자)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰")
                        .queryParam("statusId", String.valueOf(예상_상태_식별자))
                )
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("상태별 프로젝트 일정 전체 조회")
                                .description("프로젝트와 상태의 식별자를 사용하여 프로젝트 내의 모든 일정을 상태별로 조회합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("조회할 프로젝트 식별자")
                                )
                                .queryParameters(
                                        parameterWithName("statusId").type(SimpleType.NUMBER)
                                                .description("조회할 상태 식별자")
                                )
                                .responseFields(
                                        fieldWithPath("[].taskId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 일정 식별자"),
                                        fieldWithPath("[].statusId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 상태 식별자"),
                                        fieldWithPath("[].userNickname").type(JsonFieldType.STRING)
                                                .description("회원 이름"),
                                        fieldWithPath("[].taskName").type(JsonFieldType.STRING)
                                                .description("일정 이름"),
                                        fieldWithPath("[].sortOrder").type(JsonFieldType.NUMBER)
                                                .description("정렬 순서")
                                )
                                .build()
                )));

    }

    @Test
    void 일정_상세조회_API_문서를_생성한다() throws Exception {
        // given
        Long 예상_프로젝트_식별자 = 1L;
        Long 예상_일정_식별자 = 1L;
        TaskDetailResponse 예상_상세조회_응답 = TaskDetailResponseTestBuilder.일정_상세조회_응답은().이다();

        // when
        when(taskService.getTask(anyLong())).thenReturn(예상_상세조회_응답);

        // then
        mockMvc.perform(get(("/api/v1/project/{projectId}/task/{taskId}"), 예상_프로젝트_식별자, 예상_일정_식별자)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로젝트 일정 상세 조회")
                                .description("프로젝트 내에서 선택한 일정을 조회합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").type(SimpleType.NUMBER)
                                                .description("조회할 프로젝트 식별자"),
                                        parameterWithName("taskId").type(SimpleType.NUMBER)
                                                .description("조회할 일정 식별자")
                                )
                                .responseFields(
                                        fieldWithPath("taskId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 일정 식별자"),
                                        fieldWithPath("statusId").type(JsonFieldType.NUMBER)
                                                .description("프로젝트 상태 식별자"),
                                        fieldWithPath("userNickname").type(JsonFieldType.STRING)
                                                .description("회원 이름"),
                                        fieldWithPath("taskName").type(JsonFieldType.STRING)
                                                .description("일정 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("일정 내용"),
                                        fieldWithPath("sortOrder").type(JsonFieldType.NUMBER)
                                                .description("정렬 순서"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("시작일자"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("종료일자")
                                )
                                .build()
                )));

    }

    @Test
    @WithMockSecurityUser
    void 일정_변경_API_문서를_작성한다() throws Exception {
        // given
        Long 예상_프로젝트_식별자 = 1L;
        Long 예상_일정_식별자 = 1L;
        TaskEditRequest 일정_변경_요청 = TaskEditRequestTestBuilder.일정_수정_요청은().이다();

        // when
        doNothing().when(taskService).editTask(any(TaskEditCommand.class));

        // then
        mockMvc.perform(patch("/api/v1/project/{projectId}/task/{taskId}", 예상_프로젝트_식별자, 예상_일정_식별자)
                        .content(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(일정_변경_요청))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로젝트 일정 내용 변경")
                                .description("프로젝트 일정의 상태 식별자, 일정 이름, 일정 본문내용, 정렬 순서, 시작일자, 종료일자를 변경합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").description("프로젝트 식별자"),
                                        parameterWithName("taskId").description("변경할 일정 식별자")
                                )
                                .requestHeaders(HeaderDocumentation.headerWithName(
                                        org.springframework.http.HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE))
                                .requestFields(
                                        fieldWithPath("statusId").type(JsonFieldType.NUMBER)
                                                .optional()
                                                .description("프로젝트 상태 식별자"),
                                        fieldWithPath("taskName").type(JsonFieldType.STRING)
                                                .description("일정 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("일정 내용"),
                                        fieldWithPath("sortOrder").type(JsonFieldType.NUMBER)
                                                .description("정렬 순서"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("시작일자"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("종료일자")
                                )
                                .build()
                )));
    }

    @Test
    @WithMockSecurityUser
    void 일정_삭제_API_문서를_작성한다() throws Exception {
        // given
        Long 예상_프로젝트_식별자 = 1L;
        Long 예상_일정_식별자 = 1L;

        // when
        doNothing().when(taskService).deleteTask(anyLong());

        // then
        mockMvc.perform(delete("/api/v1/project/{projectId}/task/{taskId}", 예상_프로젝트_식별자, 예상_일정_식별자)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("프로젝트 일정 삭제")
                                .description("프로젝트 일정을 삭제합니다.")
                                .pathParameters(
                                        parameterWithName("projectId").description("프로젝트 식별자"),
                                        parameterWithName("taskId").description("변경할 일정 식별자")
                                )
                                .build()
                )));
    }
}
