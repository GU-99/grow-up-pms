package com.growup.pms.auth.service.oauth;

import com.growup.pms.auth.service.dto.oauth.OAuthAccessToken;
import com.growup.pms.auth.service.dto.oauth.OAuthProfile;
import com.growup.pms.user.domain.Provider;

public interface OAuth2Service {

    OAuthAccessToken requestToken(Provider provider, String code);
    OAuthProfile requestProfile(Provider provider, OAuthAccessToken accessToken);
}
