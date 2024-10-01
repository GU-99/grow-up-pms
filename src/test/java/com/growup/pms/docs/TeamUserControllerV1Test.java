package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.growup.pms.test.fixture.team.builder.TeamUserResponseTestBuilder.팀원_응답은;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.role.domain.TeamRole;
import com.growup.pms.team.controller.dto.request.RoleUpdateRequest;
import com.growup.pms.team.controller.dto.response.TeamUserResponse;
import com.growup.pms.team.service.TeamUserService;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@WithMockSecurityUser(id = 1L)
class TeamUserControllerV1Test extends ControllerSliceTestSupport {

    static final String TAG = "Team";

    @Autowired
    TeamUserService teamUserService;

    @Test
    void 팀원_목록_조회_API_문서를_생성한다() throws Exception {
        // given
        Long 기존_팀_ID = 1L;
        List<TeamUserResponse> 예상_응답 = List.of(
                팀원_응답은().사용자_식별자가(1L).닉네임이("브라운").역할명이(TeamRole.HEAD.getRoleName()).이다(),
                팀원_응답은().사용자_식별자가(2L).닉네임이("코니").역할명이(TeamRole.MATE.getRoleName()).이다());

        when(teamUserService.getAllTeamUsers(기존_팀_ID)).thenReturn(예상_응답);

        // when & then
        mockMvc.perform(get("/api/v1/team/{id}/user", 기존_팀_ID))
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(예상_응답)))
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀원 목록 조회")
                                .description("해당 팀의 팀원 목록을 조회합니다.")
                                .pathParameters(parameterWithName("id").type(SimpleType.INTEGER).description("팀 ID"))
                                .responseFields(
                                        fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("팀원 ID"),
                                        fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("팀원 닉네임"),
                                        fieldWithPath("[].roleName").type(JsonFieldType.STRING).description("팀원 역할명"))
                                .responseHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)).build())));
    }

    @Test
    void 팀_추방_API_문서를_생성한다() throws Exception {
        // given
        Long 팀_ID = 1L;
        Long 추방할_팀원_ID = 2L;

        doNothing().when(teamUserService).kickMember(팀_ID, 추방할_팀원_ID);

        // when & then
        mockMvc.perform(delete("/api/v1/team/{teamId}/user/{targetMemberId}", 팀_ID, 추방할_팀원_ID))
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀원 추방")
                                .description("해당 팀원을 추방합니다. 역할이 Mate인 팀원만 추방할 수 있습니다.")
                                .pathParameters(
                                        parameterWithName("teamId").type(SimpleType.INTEGER).description("팀 ID"),
                                        parameterWithName("targetMemberId").type(SimpleType.INTEGER).description("추방할 팀원 ID")).build())));
    }

    @Test
    void 팀원_역할_변경_API_문서를_생성한다() throws Exception {
        // given
        Long 팀_ID = 1L;
        Long 역할_변경할_팀원_ID = 2L;
        RoleUpdateRequest 역할_변경_요청 = new RoleUpdateRequest(TeamRole.MATE.getRoleName());

        doNothing().when(teamUserService).changeRole(팀_ID, 역할_변경할_팀원_ID, 역할_변경_요청.roleName());

        // when & then
        mockMvc.perform(put("/api/v1/team/{teamId}/user/{targetMemberId}/role", 팀_ID, 역할_변경할_팀원_ID)
                        .content(objectMapper.writeValueAsString(역할_변경_요청))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀원 역할 변경")
                                .description("팀장이 자신의 팀원의 역할을 변경합니다. 자신의 역할은 변경할 수 없습니다.")
                                .pathParameters(
                                        parameterWithName("teamId").type(SimpleType.INTEGER).description("팀 ID"),
                                        parameterWithName("targetMemberId").type(SimpleType.INTEGER).description("역할을 변경할 팀원 ID"))
                                .requestFields(fieldWithPath("roleName").type(JsonFieldType.STRING).description("부여할 역할명")).build())));
    }
}

