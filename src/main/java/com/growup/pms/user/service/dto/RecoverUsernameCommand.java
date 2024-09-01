package com.growup.pms.user.service.dto;

import lombok.Builder;

@Builder
public record RecoverUsernameCommand(String email, String verificationCode) {
}
