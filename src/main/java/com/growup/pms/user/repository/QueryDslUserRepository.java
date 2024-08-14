package com.growup.pms.user.repository;

import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import java.util.List;

public interface QueryDslUserRepository {
    List<UserSearchResponse> findUsersByNicknameStartingWith(String nicknamePrefix);

    List<UserTeamResponse> findAllUserTeams(Long userId);
}
