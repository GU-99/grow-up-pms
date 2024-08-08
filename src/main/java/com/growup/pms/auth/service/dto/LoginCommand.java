package com.growup.pms.auth.service.dto;

import lombok.Builder;

@Builder
public record LoginCommand(String email, String password) {
}
