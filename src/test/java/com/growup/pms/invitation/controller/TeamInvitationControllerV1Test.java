package com.growup.pms.invitation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.growup.pms.invitation.dto.TeamInvitationCreateRequest;
import com.growup.pms.invitation.service.TeamInvitationService;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class TeamInvitationControllerV1Test extends ControllerSliceTestSupport {
    @Autowired
    TeamInvitationService teamInvitationService;

    @Nested
    class 팀_초대_시에 {
        @Test
        void 성공한다() throws Exception {
            // given
            Long 초대할_팀_ID = 1L;
            Long 초대할_사용자_ID = 1L;
            Long 생성된_팀_초대_ID = 1L;
            TeamInvitationCreateRequest 팀_초대_요청 = new TeamInvitationCreateRequest(초대할_사용자_ID);

            when(teamInvitationService.sendInvitation(eq(초대할_팀_ID), any(TeamInvitationCreateRequest.class))).thenReturn(생성된_팀_초대_ID);

            // when & then
            mockMvc.perform(post("/api/v1/team/{teamId}/invitation", 초대할_팀_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(팀_초대_요청)))
                    .andExpectAll(
                            status().isCreated(),
                            header().string(HttpHeaders.LOCATION, "/api/v1/team/" + 초대할_팀_ID + "/invitation/" + 생성된_팀_초대_ID));
        }
    }
}
