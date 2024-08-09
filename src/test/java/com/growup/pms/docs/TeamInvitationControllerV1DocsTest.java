package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.invitation.controller.dto.request.TeamInvitationCreateRequest;
import com.growup.pms.invitation.domian.dto.TeamInvitationCreateCommand;
import com.growup.pms.invitation.service.TeamInvitationService;
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
@WithMockSecurityUser(id = 1L)
class TeamInvitationControllerV1DocsTest extends ControllerSliceTestSupport {
    static final String TAG = "Team Invitation";

    @Autowired
    TeamInvitationService teamInvitationService;

    @Test
    void 팀_초대_API_문서를_생성한다() throws Exception {
        // given
        Long 초대할_팀_ID = 1L;
        Long 초대할_사용자_ID = 1L;
        Long 예상하는_생성된_초대_ID = 1L;
        TeamInvitationCreateRequest 팀_초대_요청 = new TeamInvitationCreateRequest(초대할_사용자_ID);

        when(teamInvitationService.sendInvitation(eq(초대할_팀_ID), any(TeamInvitationCreateCommand.class))).thenReturn(예상하는_생성된_초대_ID);

        // when & then
        mockMvc.perform(post("/api/v1/team/{teamId}/invitation", 초대할_팀_ID)
                .content(objectMapper.writeValueAsString(팀_초대_요청))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/v1/team/1/invitation/1"))
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀 초대")
                                .description("해당 사용자를 특정 팀에 초대합니다.")
                                .pathParameters(parameterWithName("teamId").type(SimpleType.NUMBER).description("초대할 팀 ID"))
                                .requestFields(fieldWithPath("id").type(JsonFieldType.NUMBER).description("초대할 사용자 ID"))
                                .requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 액세스 토큰"))
                                .build())));
    }

    @Test
    void 팀_초대_승낙_API_문서를_생성한다() throws Exception {
        // given
        Long 초대된_팀_ID = 1L;
        Long 초대된_사용자_ID = 1L;

        doNothing().when(teamInvitationService).acceptInvitation(초대된_팀_ID, 초대된_사용자_ID);

        // when & then
        mockMvc.perform(post("/api/v1/team/{teamId}/invitation/accept", 초대된_팀_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀 초대 승낙")
                                .description("사용자가 팀 초대를 승낙합니다.")
                                .pathParameters(parameterWithName("teamId").type(SimpleType.NUMBER).description("초대된 팀 ID"))
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 액세스 토큰"))
                                .build())));
    }

    @Test
    void 팀_초대_거절_API_문서를_생성한다() throws Exception {
        // given
        Long 초대된_팀_ID = 1L;
        Long 초대된_사용자_ID = 1L;

        doNothing().when(teamInvitationService).declineInvitation(초대된_팀_ID, 초대된_사용자_ID);

        // when & then
        mockMvc.perform(post("/api/v1/team/{teamId}/invitation/decline", 초대된_팀_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer 액세스 토큰"))
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀 초대 거절")
                                .description("사용자가 팀 초대를 거절합니다.")
                                .pathParameters(parameterWithName("teamId").type(SimpleType.NUMBER).description("초대된 팀 ID"))
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 액세스 토큰"))
                                .build())));
    }
}
