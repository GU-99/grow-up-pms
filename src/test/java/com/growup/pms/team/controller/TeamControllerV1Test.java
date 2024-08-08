package com.growup.pms.team.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.growup.pms.test.fixture.team.TeamCreateRequestTestBuilder.팀_생성_요청은;
import static com.growup.pms.test.fixture.team.TeamUpdateRequestTestBuilder.팀_수정_요청은;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.team.controller.dto.request.TeamCreateRequest;
import com.growup.pms.team.controller.dto.request.TeamUpdateRequest;
import com.growup.pms.team.controller.dto.response.TeamResponse;
import com.growup.pms.team.service.TeamService;
import com.growup.pms.team.service.dto.TeamCreateCommand;
import com.growup.pms.team.service.dto.TeamUpdateCommand;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class TeamControllerV1Test extends ControllerSliceTestSupport {
    static final String TAG = "Team";

    @Autowired
    TeamService teamService;

    @Nested
    class 사용자가_팀을_조회_시에 {
        @Test
        void 성공한다() throws Exception {
            // given
            Long 기존_팀_ID = 1L;
            TeamResponse 예상_응답 = TeamResponse.builder()
                    .name("구구구")
                    .content("안녕하세요, 구구구입니다!")
                    .build();

            when(teamService.getTeam(기존_팀_ID)).thenReturn(예상_응답);

            // when & then
            mockMvc.perform(get("/api/v1/team/{id}", 기존_팀_ID))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.name").value(예상_응답.name()),
                            jsonPath("$.content").value(예상_응답.content()))
                    .andDo(docs.document(resource(
                            ResourceSnippetParameters.builder()
                                    .tag(TAG)
                                    .summary("팀 조회")
                                    .description("팀 정보를 조회합니다.")
                                    .pathParameters(parameterWithName("id").description("팀 아이디"))
                                    .responseFields(
                                            fieldWithPath("name").description("팀 이름"),
                                            fieldWithPath("content").description("팀 소개"))
                                    .responseHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)).build())));
        }

        @Test
        void 존재하지_않는_팀_조회_시_404_에러를_반환한다() throws Exception {
            // given
            Long teamId = 1L;

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
                                    .description("팀을 생성합니다.")
                                    .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                    .requestFields(
                                            fieldWithPath("name").description("팀 이름"),
                                            fieldWithPath("content").description("팀 소개"))
                                    .responseHeaders(headerWithName(HttpHeaders.LOCATION).description("생성된 팀의 URL")).build())));
        }

        @Test
        void 유효하지_않은_입력으로_팀_생성_시_400_에러를_반환한다() throws Exception {
            // given
            String 유효하지_않은_이름 = "!#$&-_이름";
            TeamCreateRequest 팀_생성_요청 = 팀_생성_요청은().이름이(유효하지_않은_이름).이다();

            // when & then
            mockMvc.perform(post("/api/v1/team")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(팀_생성_요청)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class 사용자가_팀을_변경_시에 {
        @Test
        void 성공한다() throws Exception {
            // given
            Long 기존_팀_ID = 1L;
            TeamCreateRequest 팀_생성_요청 = 팀_생성_요청은().이다();

            doNothing().when(teamService).updateTeam(eq(기존_팀_ID), any(TeamUpdateCommand.class));

            // when & then
            mockMvc.perform(patch("/api/v1/team/{id}", 기존_팀_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(팀_생성_요청)))
                .andExpect(status().isNoContent())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("팀 변경")
                                .description("팀의 정보를 변경합니다.")
                                .pathParameters(parameterWithName("id").description("팀 아이디"))
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .requestFields(
                                        fieldWithPath("name").description("팀 이름"),
                                        fieldWithPath("content").description("팀 소개")).build())));
        }

        @Test
        void 유효하지_않은_입력으로_팀_변경_시_400_에러를_반환한다() throws Exception {
            // given
            Long 기존_팀_ID = 1L;
            String 유효하지_않은_팀_이름 = "!#$&-_이름";
            TeamUpdateRequest 팀_수정_요청 = 팀_수정_요청은().이름이(유효하지_않은_팀_이름).이다();

            doNothing().when(teamService).updateTeam(eq(기존_팀_ID), any(TeamUpdateCommand.class));

            // when & then
            mockMvc.perform(patch("/api/v1/team/" + 기존_팀_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(팀_수정_요청)))
                    .andExpect(status().isBadRequest());
        }
    }

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
