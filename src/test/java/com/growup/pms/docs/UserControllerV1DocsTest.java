package com.growup.pms.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.growup.pms.test.fixture.user.builder.RecoverPasswordRequestTestBuilder.비밀번호_찾기_요청은;
import static com.growup.pms.test.fixture.user.builder.RecoverUsernameRequestTestBuilder.아이디_찾기_요청은;
import static com.growup.pms.test.fixture.user.builder.UserCreateRequestTestBuilder.가입하는_사용자는;
import static com.growup.pms.test.fixture.user.builder.UserResponseTestBuilder.사용자_조회_응답은;
import static com.growup.pms.test.fixture.user.builder.UserTeamResponseTestBuilder.가입한_팀_응답은;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import com.growup.pms.test.support.ControllerSliceTestSupport;
import com.growup.pms.user.controller.dto.request.RecoverPasswordRequest;
import com.growup.pms.user.controller.dto.request.RecoverUsernameRequest;
import com.growup.pms.user.controller.dto.request.UserCreateRequest;
import com.growup.pms.user.controller.dto.response.RecoverPasswordResponse;
import com.growup.pms.user.controller.dto.response.RecoverUsernameResponse;
import com.growup.pms.user.controller.dto.response.UserResponse;
import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import com.growup.pms.user.service.UserService;
import com.growup.pms.user.service.dto.RecoverPasswordCommand;
import com.growup.pms.user.service.dto.RecoverUsernameCommand;
import com.growup.pms.user.service.dto.UserCreateCommand;
import java.util.List;
import java.util.Map;
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
    void 현재_사용자_정보_조회_API_문서를_생성한다() throws Exception {
        // given
        Long 현재_사용자_ID = 1L;
        UserResponse 예상_응답 = 사용자_조회_응답은().이다();

        when(userService.getUser(현재_사용자_ID)).thenReturn(예상_응답);

        // when & then
        mockMvc.perform(get("/api/v1/user/me"))
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("현재 사용자 정보 조회")
                                .description("현재 로그인한 사용자의 정보를 조회합니다.")
                                .responseFields(
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("식별자"),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("provider").type(JsonFieldType.STRING).description("인증 프로바이더"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("bio").type(JsonFieldType.STRING).description("자기소개"),
                                        fieldWithPath("profileImageName").type(JsonFieldType.STRING).description("프로필 이미지 이름"),
                                        fieldWithPath("links").type(JsonFieldType.ARRAY).description("링크 목록"))
                                .responseHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .build())));
    }

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
                                        fieldWithPath("[].teamName").type(JsonFieldType.STRING).description("팀 이름"),
                                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("팀 소개"),
                                        fieldWithPath("[].creator").type(JsonFieldType.STRING).description("팀장 닉네임"),
                                        fieldWithPath("[].creatorId").type(JsonFieldType.NUMBER).description("팀장 ID"),
                                        fieldWithPath("[].isPendingApproval").type(JsonFieldType.BOOLEAN).description("가입 대기 여부"),
                                        fieldWithPath("[].roleName").type(JsonFieldType.STRING).description("팀 내에서의 역할"))
                                .responseHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .build())));
    }

    @Test
    void 사용자_일반_회원가입_API_문서를_생성한다() throws Exception {
        // given
        Long 사용자_ID = 1L;
        UserCreateRequest 사용자_생성_요청 = 가입하는_사용자는().이다();

        when(userService.save(any(UserCreateCommand.class))).thenReturn(사용자_ID);

        // when & then
        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(사용자_생성_요청)))
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/v1/user/" + 사용자_ID)
                )
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("사용자 일반 회원가입")
                                .description("사용자의 계정을 서버에 등록합니다.")
                                .requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("bio").type(JsonFieldType.STRING).description("자기소개"),
                                        fieldWithPath("profileImageName").type(JsonFieldType.STRING).description("프로필 이미지 이름"),
                                        fieldWithPath("links").type(JsonFieldType.ARRAY).description("사용자 링크"),
                                        fieldWithPath("verificationCode").type(JsonFieldType.STRING).description("인증코드"))
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .build())));
    }

    @Test
    void 인증코드_전송_API_문서를_생성한다() throws Exception {
        // given
        Long 사용자_ID = 1L;
        Map<String, String> 가입하려는_사용자_이메일 = Map.of("email", "test@example.org");

        when(userService.save(any(UserCreateCommand.class))).thenReturn(사용자_ID);

        // when & then
        mockMvc.perform(post("/api/v1/user/verify/send")
                        .content(objectMapper.writeValueAsString(가입하려는_사용자_이메일))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("인증코드 전송")
                                .description("사용자의 이메일에 인증코드를 전송합니다. 만료기간은 3분입니다.")
                                .requestFields(fieldWithPath("email").type(JsonFieldType.STRING).description("인증하려는 사용자 이메일"))
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .build())));
    }

    @Test
    void 아이디_찾기_API_문서를_생성한다() throws Exception {
        // given
        String 복구된_아이디 = "brown";
        RecoverUsernameRequest 아이디_찾기_요청 = 아이디_찾기_요청은().이다();
        RecoverUsernameResponse 아이디_찾기_응답 = new RecoverUsernameResponse(복구된_아이디);

        when(userService.recoverUsername(any(RecoverUsernameCommand.class))).thenReturn(아이디_찾기_응답);

        // when & then
        mockMvc.perform(post("/api/v1/user/recover/username")
                        .content(objectMapper.writeValueAsString(아이디_찾기_요청))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("아이디 찾기")
                                .description("이메일 인증을 통해 사용자의 아이디를 찾는다.")
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("가입 시 입력한 이메일"),
                                        fieldWithPath("verificationCode").type(JsonFieldType.STRING).description("이메일로 전송된 인증번호"))
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .responseFields(fieldWithPath("username").type(JsonFieldType.STRING).description("복구된 사용자의 아이디"))
                                .build())));
    }

    @Test
    void 비밀번호_찾기_API_문서를_생성한다() throws Exception {
        // given
        String 새로_발급된_비밀번호 = "napl1m!A";
        RecoverPasswordRequest 비밀번호_찾기_요청 = 비밀번호_찾기_요청은().이다();
        RecoverPasswordResponse 비밀번호_찾기_응답 = new RecoverPasswordResponse(새로_발급된_비밀번호);

        when(userService.recoverPassword(any(RecoverPasswordCommand.class))).thenReturn(비밀번호_찾기_응답);

        // when & then
        mockMvc.perform(post("/api/v1/user/recover/password")
                        .content(objectMapper.writeValueAsString(비밀번호_찾기_요청))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(docs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .summary("비밀번호 찾기")
                                .description("이메일 인증과 아이디를 통해서 사용자에게 임시 비밀번호를 발급한다.")
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("가입 시 입력한 이메일"),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("가입 시 입력한 아이디"),
                                        fieldWithPath("verificationCode").type(JsonFieldType.STRING).description("이메일로 전송된 인증번호"))
                                .requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE))
                                .responseFields(fieldWithPath("password").type(JsonFieldType.STRING).description("임시로 발급된 비밀번호"))
                                .build())));
    }
}
