package com.growup.pms.auth.service.dto.oauth;

import lombok.Builder;

@Builder
public record OauthUserLoginCommand(String code) {
}
