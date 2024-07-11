package com.growup.pms.test.fixture.team;

import com.growup.pms.team.domain.Team;
import com.growup.pms.team.dto.TeamCreateRequest;
import com.growup.pms.team.dto.TeamResponse;
import com.growup.pms.team.dto.TeamUpdateRequest;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.test.util.ReflectionTestUtils;

public class TeamFixture {
    public static final Long DEFAULT_TEAM_ID = 1L;
    public static final String DEFAULT_TEAM_NAME = "구구구";
    public static final String DEFAULT_TEAM_CONTENT = "안녕하세요, 구구구입니다!";

    public static Team createDefaultTeam() {
        return createTeamWithId(DEFAULT_TEAM_ID);
    }

    public static Team createTeamWithId(Long id) {
        Team team = createDefaultTeamBuilder().build();
        ReflectionTestUtils.setField(team, "id", id);
        return team;
    }

    public static Team.TeamBuilder createDefaultTeamBuilder() {
        return Team.builder()
                .name(DEFAULT_TEAM_NAME)
                .content(DEFAULT_TEAM_CONTENT);
    }

    public static TeamCreateRequest createDefaultTeamCreateRequest() {
        return createDefaultTeamCreateRequestBuilder().build();
    }

    public static TeamCreateRequest.TeamCreateRequestBuilder createDefaultTeamCreateRequestBuilder() {
        return TeamCreateRequest.builder()
                .name(DEFAULT_TEAM_NAME)
                .content(DEFAULT_TEAM_CONTENT);
    }

    public static TeamUpdateRequest createDefaultTeamUpdateRequest() {
        return createDefaultTeamUpdateRequestBuilder().build();
    }

    public static TeamUpdateRequest.TeamUpdateRequestBuilder createDefaultTeamUpdateRequestBuilder() {
        return TeamUpdateRequest.builder()
                .name(JsonNullable.of(DEFAULT_TEAM_NAME))
                .content(JsonNullable.of(DEFAULT_TEAM_CONTENT));
    }

    public static TeamResponse createDefaultTeamResponse() {
        return createDefaultTeamResponseBuilder().build();
    }

    public static TeamResponse.TeamResponseBuilder createDefaultTeamResponseBuilder() {
        return TeamResponse.builder()
                .name(DEFAULT_TEAM_NAME)
                .content(DEFAULT_TEAM_CONTENT);
    }
}
