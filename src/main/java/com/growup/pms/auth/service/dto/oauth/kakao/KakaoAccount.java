package com.growup.pms.auth.service.dto.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(value = SnakeCaseStrategy.class)
public record KakaoAccount(
        String email,
        Boolean isEmailVerified,
        Boolean hasEmail,
        Boolean profileNicknameNeedsAgreement,
        Boolean profileImageNeedsAgreement,
        Boolean emailNeedsAgreement,
        Boolean isEmailValid,
        Profile profile
) {
}
