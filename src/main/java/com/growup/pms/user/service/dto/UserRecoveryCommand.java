package com.growup.pms.user.service.dto;

import lombok.Builder;

@Builder
public record UserRecoveryCommand(String email, String verificationCode) {
}
