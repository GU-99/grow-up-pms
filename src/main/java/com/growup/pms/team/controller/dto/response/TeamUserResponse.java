package com.growup.pms.team.controller.dto.response;

import lombok.Builder;

@Builder
public record TeamUserResponse(Long userId, String nickname, String roleName) {
}
