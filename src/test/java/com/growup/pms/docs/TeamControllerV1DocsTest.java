package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.growup.pms.test.fixture.team.TeamCreateRequestTestBuilder.팀_생성_요청은;
import static com.growup.pms.test.fixture.team.TeamResponseTestBuilder.팀_생성_응답은;
import static com.growup.pms.test.fixture.team.TeamUpdateRequestTestBuilder.팀_변경_요청은;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.team.controller.dto.request.TeamCreateRequest;
import com.growup.pms.team.controller.dto.request.TeamUpdateRequest;
import com.growup.pms.team.controller.dto.response.TeamResponse;
import com.growup.pms.team.service.TeamService;
import com.growup.pms.team.service.dto.TeamCreateCommand;
import com.growup.pms.team.service.dto.TeamUpdateCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class TeamControllerV1DocsTest extends ControllerSliceTestSupport {
    static final String TAG = "Team";

    @Autowired
    TeamService teamService;

    @Test
    @WithMockSecurityUser(id = 1L)
    void 팀_생성_API_문서를_생성한다() throws Exception {
        // given
        Long 팀장_ID = 1L;
        Long 예상_팀_ID = 1L;
        TeamCreateRequest 팀_생성_요청 = 팀_생성_요청은().이다();

        when(teamService.createTeam(eq(팀장_ID), any(TeamCreateCommand.class))).thenReturn(예상_팀_ID);

        // when & then
        mockMvc.perform(post("/api/v1/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(팀_생성_요청)))
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/v1/team/" + 예상_팀_ID))
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀 생성")
                                .description("사용자가 새로운 팀을 생성합니다. 생성 시, 다른 사용자를 함께 팀으로 초대할 수 있습니다.")
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("팀 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("팀 소개"),
                                        fieldWithPath("creatorId").type(JsonFieldType.NUMBER).description("팀 생성자 ID"),
                                        fieldWithPath("coworkers").type(JsonFieldType.ARRAY).description("초대된 사용자 목록"),
                                        fieldWithPath("coworkers[].id").type(JsonFieldType.NUMBER).description("초대된 사용자 ID"),
                                        fieldWithPath("coworkers[].role").type(JsonFieldType.STRING).description("초대된 사용자의 역할"))
                                .responseHeaders(headerWithName(HttpHeaders.LOCATION).description("생성된 팀의 URL")).build())));
    }

    @Test
    void 팀_조회_API_문서를_생성한다() throws Exception {
        // given
        Long 기존_팀_ID = 1L;
        TeamResponse 예상_응답 = 팀_생성_응답은().이다();

        when(teamService.getTeam(기존_팀_ID)).thenReturn(예상_응답);

        // when & then
        mockMvc.perform(get("/api/v1/team/{id}", 기존_팀_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name").value(예상_응답.name()),
                        jsonPath("$.content").value(예상_응답.content()),
                        jsonPath("$.creatorId").value(예상_응답.creatorId()))
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀 조회")
                                .description("해당 팀 정보를 조회합니다.")
                                .pathParameters(parameterWithName("id").type(SimpleType.INTEGER).description("팀 아이디"))
                                .responseFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("팀 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("팀 소개"),
                                        fieldWithPath("creatorId").type(JsonFieldType.NUMBER).description("팀 생성자 ID"))
                                .responseHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)).build())));
    }

    @Test
    void 팀_변경_API_문서를_생성한다() throws Exception {
        // given
        Long 기존_팀_ID = 1L;
        TeamUpdateRequest 팀_변경_요청 = 팀_변경_요청은().이다();

        doNothing().when(teamService).updateTeam(eq(기존_팀_ID), any(TeamUpdateCommand.class));

        // when & then
        mockMvc.perform(patch("/api/v1/team/{id}", 기존_팀_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(팀_변경_요청)))
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀 변경")
                                .description("해당 팀의 정보를 변경합니다.")
                                .pathParameters(parameterWithName("id").type(SimpleType.INTEGER).description("팀 아이디"))
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("팀 이름"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("팀 소개")).build())));
    }

    @Test
    void 팀_제거_API_문서를_생성한다() throws Exception {
        // given
        Long 기존_팀_ID = 1L;

        doNothing().when(teamService).deleteTeam(기존_팀_ID);

        // when & then
        mockMvc.perform(delete("/api/v1/team/{id}", 기존_팀_ID))
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀 제거")
                                .description("해당 팀을 제거합니다.")
                                .pathParameters(parameterWithName("id").type(SimpleType.INTEGER).description("제거할 팀 ID")).build())));
    }
}
