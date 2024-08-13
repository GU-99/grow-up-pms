package com.growup.pms.user.repository;

import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import java.util.List;

public interface QueryDslUserRepository {
    List<UserSearchResponse> findUsersByNicknameStartingWith(String nicknamePrefix);
}
