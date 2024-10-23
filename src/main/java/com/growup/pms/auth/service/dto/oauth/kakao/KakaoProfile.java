package com.growup.pms.auth.service.dto.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growup.pms.auth.service.dto.oauth.OauthProfile;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
public record KakaoProfile(
        Long id,
        String connectedAt,
        Properties properties,
        KakaoAccount kakaoAccount
) implements OauthProfile {

    @Override
    public String getNickname() {
        return kakaoAccount.profile().nickname();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email();
    }
}
