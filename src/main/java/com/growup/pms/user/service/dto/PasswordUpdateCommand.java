package com.growup.pms.user.service.dto;

import lombok.Builder;

@Builder
public record PasswordUpdateCommand(String password, String newPassword) {
}
