package com.growup.pms.auth.service.dto.oauth.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growup.pms.auth.service.dto.oauth.OauthProfile;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
public record GoogleProfile(
        String id,
        String email,
        Boolean verifiedEmail,
        String picture,
        String hd
) implements OauthProfile {

    @Override
    public String getNickname() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
