package com.growup.pms.auth.controller.dto.response;

import lombok.Builder;

@Builder
public record AccessTokenResponse(String accessToken) {
}
