package com.growup.pms.project.controller.dto.response;

import lombok.Builder;

@Builder
public record ProjectUserResponse(Long userId, String nickname, String roleName) {

}
