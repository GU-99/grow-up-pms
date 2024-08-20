package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.growup.pms.test.fixture.user.UserTeamResponseTestBuilder.가입한_팀_응답은;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import com.growup.pms.user.service.UserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class UserControllerV1DocsTest extends ControllerSliceTestSupport {
    static final String TAG = "User";

    @Autowired
    UserService userService;

    @Test
    @WithMockSecurityUser(id = 1L)
    void 가입한_팀_목록_조회_API_문서를_생성한다() throws Exception {
        // given
        Long 사용자_ID = 1L;
        List<UserTeamResponse> 예상_응답 = List.of(가입한_팀_응답은().이다());

        when(userService.getAllUserTeams(사용자_ID)).thenReturn(예상_응답);

        // when & then
        mockMvc.perform(get("/api/v1/user/team"))
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("가입한 팀 목록 조회")
                                .description("가입한 팀이거나 가입 대기 중인 팀의 목록을 조회합니다.")
                                .responseFields(
                                        fieldWithPath("[].teamId").type(JsonFieldType.NUMBER).description("팀 ID"),
                                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("팀 이름"),
                                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("팀 소개"),
                                        fieldWithPath("[].creator").type(JsonFieldType.STRING).description("팀장 닉네임"),
                                        fieldWithPath("[].isPendingApproval").type(JsonFieldType.BOOLEAN).description("가입 대기 여부"),
                                        fieldWithPath("[].role").type(JsonFieldType.STRING).description("팀 내에서의 역할"))
                                .responseHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .build())));
    }
}
