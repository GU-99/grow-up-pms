package com.growup.pms.auth.service.dto.oauth.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoAccessToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String scope;
    private int expires_in;
    private int refresh_token_expires_in;

    @Builder
    public KakaoAccessToken(String access_token, String token_type, String refresh_token, String scope, int expires_in,
                            int refresh_token_expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.scope = scope;
        this.expires_in = expires_in;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }
}
