package com.growup.pms.task.controller.dto.response;

import lombok.Builder;

@Builder
public record TaskUserResponse(Long userId, String nickname, String roleName) {

}
