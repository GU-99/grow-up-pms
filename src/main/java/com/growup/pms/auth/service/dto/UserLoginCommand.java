package com.growup.pms.auth.service.dto;

import lombok.Builder;

@Builder
public record UserLoginCommand(String username, String password) {
}
