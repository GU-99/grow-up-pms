package com.growup.pms.auth.service.dto.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(value = SnakeCaseStrategy.class)
public class KakaoAccount {
    private String email;
    private Boolean isEmailVerified;
    private Boolean hasEmail;
    private Boolean profileNicknameNeedsAgreement;
    private Boolean profileImageNeedsAgreement;
    private Boolean emailNeedsAgreement;
    private Boolean isEmailValid;
    private Profile profile;

    @Builder
    public KakaoAccount(String email, Boolean isEmailVerified, Boolean hasEmail,
                        Boolean profileNicknameNeedsAgreement, Boolean profileImageNeedsAgreement,
                        Boolean emailNeedsAgreement, Boolean isEmailValid, Profile profile) {
        this.email = email;
        this.isEmailValid = isEmailValid;
        this.hasEmail = hasEmail;
        this.profileImageNeedsAgreement = profileImageNeedsAgreement;
        this.profileNicknameNeedsAgreement = profileNicknameNeedsAgreement;
        this.emailNeedsAgreement = emailNeedsAgreement;
        this.isEmailVerified = isEmailVerified;
        this.profile = profile;
    }
}
