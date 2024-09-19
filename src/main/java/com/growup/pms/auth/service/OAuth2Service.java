package com.growup.pms.auth.service;

import com.growup.pms.auth.service.dto.oauth.OAuthAccessToken;
import com.growup.pms.auth.service.dto.oauth.OAuthProfile;

public interface OAuth2Service {

    OAuthAccessToken requestToken(String provider, String code);
    OAuthProfile requestProfile(String provider, OAuthAccessToken accessToken);
}
