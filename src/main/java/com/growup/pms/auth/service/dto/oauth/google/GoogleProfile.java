package com.growup.pms.auth.service.dto.oauth.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growup.pms.auth.service.dto.oauth.OAuthProfile;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
public record GoogleProfile(
        String id,
        String email,
        Boolean verifiedEmail,
        String picture,
        String hd
) implements OAuthProfile {

    @Override
    public String getNickname() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
