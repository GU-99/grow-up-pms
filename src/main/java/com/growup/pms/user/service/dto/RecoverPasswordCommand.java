package com.growup.pms.user.service.dto;

import lombok.Builder;

@Builder
public record RecoverPasswordCommand(String email, String username, String verificationCode) {
}
