package com.growup.pms.auth.service.oauth;

import com.growup.pms.auth.service.dto.oauth.OauthAccessToken;
import com.growup.pms.auth.service.dto.oauth.OauthProfile;
import com.growup.pms.user.domain.Provider;

public interface Oauth2Service {

    OauthAccessToken requestToken(Provider provider, String code);

    OauthProfile requestProfile(Provider provider, OauthAccessToken accessToken);
}
