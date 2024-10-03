package com.growup.pms.project.controller.dto.response;

import lombok.Builder;

@Builder
public record ProjectUserSearchResponse(Long userId, String nickname) {

}
