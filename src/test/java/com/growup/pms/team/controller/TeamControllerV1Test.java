package com.growup.pms.team.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.team.service.TeamService;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class TeamControllerV1Test extends ControllerSliceTestSupport {
    static final String TAG = "Team";

    @Autowired
    TeamService teamService;



    @Nested
    class 사용자가_팀을_제거_시에 {
        @Test
        void 성공한다() throws Exception {
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
                                    .description("팀을 제거합니다.")
                                    .pathParameters(parameterWithName("id").description("제거할 팀 ID")).build())));
        }

        @Test
        void 존재하지_않는_팀_제거_시_404_에러를_반환한다() throws Exception {
            // given
            Long 존재하지_않는_팀_ID = 1L;

            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)).when(teamService).deleteTeam(존재하지_않는_팀_ID);

            // when & then
            mockMvc.perform(delete("/api/v1/team/" + 존재하지_않는_팀_ID))
                    .andExpect(status().isNotFound());
        }
    }
}
