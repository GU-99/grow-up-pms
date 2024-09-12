package com.growup.pms.auth.service.dto.oauth.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class KakaoProfile {
    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Builder
    public KakaoProfile(Long id, String connected_at, Properties properties, KakaoAccount kakao_account) {
        this.id = id;
        this.connected_at = connected_at;
        this.properties = properties;
        this.kakao_account = kakao_account;
    }
}
