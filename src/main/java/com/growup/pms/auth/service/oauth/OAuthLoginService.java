package com.growup.pms.auth.service.oauth;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.service.RefreshTokenService;
import com.growup.pms.auth.service.dto.oauth.OAuthAccessToken;
import com.growup.pms.auth.service.dto.oauth.OAuthProfile;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import com.growup.pms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final KakaoOAuth2Service kakaoOAuth2Service;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService redisRefreshTokenService;

    public TokenResponse authenticate(Provider provider, String code) {
        OAuth2Service oAuth2Service = getOAuth2Service(provider);

        OAuthAccessToken accessToken = oAuth2Service.requestToken(provider, code);
        OAuthProfile profile = oAuth2Service.requestProfile(provider, accessToken);

        String email = profile.getEmail();
        String nickname = profile.getNickname();

        User user = userRepository.findByEmail(email).orElseGet(() -> joinUser(email, nickname, provider));
        SecurityUser securityUser = convertSecurityUser(user);

        TokenResponse newToken = jwtTokenProvider.generateToken(securityUser);
        redisRefreshTokenService.save(securityUser.getId(), newToken.refreshToken());

        return newToken;
    }

    private OAuth2Service getOAuth2Service(Provider provider) {
        return switch (provider) {
            case KAKAO -> kakaoOAuth2Service;
            case GOOGLE -> googleOAuth2Service;
            default -> throw new BusinessException(ErrorCode.INVALID_PROVIDER);
        };
    }

    private SecurityUser convertSecurityUser(User user) {
        return SecurityUser.builder()
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

    private User joinUser(String email, String nickname, Provider provider) {
        User user = User.builder()
                .provider(provider)
                .email(email)
                .username(email)
                .profile(UserProfile.builder()
                        .nickname(nickname)
                        .build())
                .build();

        return userRepository.save(user);
    }
}
