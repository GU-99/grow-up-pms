package com.growup.pms.user.repository;

import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import java.util.List;

public interface CustomizedUserRepository {
    List<UserSearchResponse> findUsersByNicknameStartingWith(String nicknamePrefix);
}
