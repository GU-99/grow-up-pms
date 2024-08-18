package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.growup.pms.team.service.TeamUserService;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@WithMockSecurityUser(id = 1L)
class TeamUserControllerV1Test extends ControllerSliceTestSupport {
    static final String TAG = "Team";

    @Autowired
    TeamUserService teamUserService;

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
}

