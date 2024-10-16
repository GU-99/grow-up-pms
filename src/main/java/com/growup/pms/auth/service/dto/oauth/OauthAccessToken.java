package com.growup.pms.auth.service.dto.oauth;

public interface OauthAccessToken {

    String getAccessToken();

    String getRefreshToken();

    String getTokenType();

    String getScope();

    int getExpiresIn();
}
