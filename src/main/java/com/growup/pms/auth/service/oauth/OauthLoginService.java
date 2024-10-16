package com.growup.pms.auth.service.oauth;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.service.RefreshTokenService;
import com.growup.pms.auth.service.dto.oauth.OauthAccessToken;
import com.growup.pms.auth.service.dto.oauth.OauthProfile;
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
public class OauthLoginService {

    private final KakaoOauth2Service kakaoOauth2Service;
    private final GoogleOauth2Service googleOauth2Service;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService redisRefreshTokenService;

    public TokenResponse authenticate(Provider provider, String code) {
        Oauth2Service oAuth2Service = getOauth2Service(provider);

        OauthAccessToken accessToken = oAuth2Service.requestToken(provider, code);
        OauthProfile profile = oAuth2Service.requestProfile(provider, accessToken);

        String email = profile.getEmail();
        String nickname = profile.getNickname();

        User user = userRepository.findByEmail(email).orElseGet(() -> joinUser(email, nickname, provider));
        SecurityUser securityUser = convertSecurityUser(user);

        TokenResponse newToken = jwtTokenProvider.generateToken(securityUser);
        redisRefreshTokenService.save(securityUser.getId(), newToken.refreshToken());

        return newToken;
    }

    private Oauth2Service getOauth2Service(Provider provider) {
        return switch (provider) {
            case KAKAO -> kakaoOauth2Service;
            case GOOGLE -> googleOauth2Service;
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
