package com.growup.pms.team.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.team.dto.TeamCreateRequest;
import com.growup.pms.team.dto.TeamResponse;
import com.growup.pms.team.dto.TeamUpdateRequest;
import com.growup.pms.team.service.TeamService;
import com.growup.pms.test.DefaultControllerSliceTest;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.fixture.team.TeamFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@WithMockUser
class TeamControllerV1Test extends DefaultControllerSliceTest {
    @Autowired
    TeamService teamService;

    @Autowired
    MockMvc mockMvc;

    @Nested
    class 사용자가_팀을_조회_시에 {

        @Test
        void 성공한다() throws Exception {
            // given
            Long teamId = TeamFixture.DEFAULT_TEAM_ID;
            TeamResponse expectedResult = TeamFixture.createDefaultTeamResponse();

            when(teamService.getTeam(teamId)).thenReturn(expectedResult);

            // when & then
            mockMvc.perform(get("/api/v1/team/" + teamId))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.name").value(expectedResult.getName()),
                            jsonPath("$.content").value(expectedResult.getContent())
                    );
        }

        @Test
        void 존재하지_않는_팀_조회_시_404_에러를_반환한다() throws Exception {
            // given
            Long teamId = TeamFixture.DEFAULT_TEAM_ID;

            when(teamService.getTeam(teamId)).thenThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

            // when & then
            mockMvc.perform(get("/api/v1/team/" + teamId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class 사용자가_팀을_생성_시에 {

        @Test
        @WithMockSecurityUser(id = 1L)
        void 성공한다() throws Exception {
            // given
            Long creatorId = 1L;
            Long expectedTeamId = TeamFixture.DEFAULT_TEAM_ID;
            TeamCreateRequest request = TeamFixture.createDefaultTeamCreateRequest();

            when(teamService.createTeam(eq(creatorId), any(TeamCreateRequest.class))).thenReturn(expectedTeamId);

            // when & then
            mockMvc.perform(post("/api/v1/team")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andExpectAll(
                            status().isCreated(),
                            header().string(HttpHeaders.LOCATION, "/api/v1/team/" + expectedTeamId)
                    );
        }

        @Test
        void 유효하지_않은_입력으로_팀_생성_시_400_에러를_반환한다() throws Exception {
            // given
            String invalidTeamName = "!#$&-_이름";
            TeamCreateRequest request = TeamFixture.createDefaultTeamCreateRequestBuilder()
                    .name(invalidTeamName)
                    .build();

            // when & then
            mockMvc.perform(post("/api/v1/team")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class 사용자가_팀을_변경_시에 {

        @Test
        void 성공한다() throws Exception {
            // given
            Long teamId = TeamFixture.DEFAULT_TEAM_ID;
            TeamCreateRequest request = TeamFixture.createDefaultTeamCreateRequest();

            doNothing().when(teamService).updateTeam(eq(teamId), any(TeamUpdateRequest.class));

            // when & then
            mockMvc.perform(patch("/api/v1/team/" + teamId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .with(csrf()))
                .andExpect(status().isNoContent());
        }

        @Test
        void 유효하지_않은_입력으로_팀_변경_시_400_에러를_반환한다() throws Exception {
            // given
            Long teamId = TeamFixture.DEFAULT_TEAM_ID;
            String invalidTeamName = "!#$&-_이름";
            TeamUpdateRequest request = TeamFixture.createDefaultTeamUpdateRequestBuilder()
                    .name(JsonNullable.of(invalidTeamName))
                    .build();

            doNothing().when(teamService).updateTeam(eq(teamId), any(TeamUpdateRequest.class));

            // when & then
            mockMvc.perform(patch("/api/v1/team/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class 사용자가_팀을_제거_시에 {

        @Test
        void 성공한다() throws Exception {
            // given
            Long teamId = TeamFixture.DEFAULT_TEAM_ID;

            doNothing().when(teamService).deleteTeam(teamId);

            // when & then
            mockMvc.perform(delete("/api/v1/team/" + teamId).with(csrf()))
                    .andExpect(status().isNoContent());
        }

        @Test
        void 존재하지_않는_팀_제거_시_404_에러를_반환한다() throws Exception {
            // given
            Long teamId = TeamFixture.DEFAULT_TEAM_ID;

            doThrow(new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)).when(teamService).deleteTeam(teamId);

            // when & then
            mockMvc.perform(delete("/api/v1/team/" + teamId).with(csrf()))
                    .andExpect(status().isNotFound());
        }
    }
}
