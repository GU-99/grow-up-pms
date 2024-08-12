package com.growup.pms.common.security.jwt.dto;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken, String refreshToken) {
}
