package com.growup.pms.team.controller.dto.response;

import lombok.Builder;

@Builder
public record TeamUserSearchResponse(Long userId, String nickname) {
}
