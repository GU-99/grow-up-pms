package com.growup.pms.auth.service;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.service.dto.oauth.google.GoogleAccessToken;
import com.growup.pms.auth.service.dto.oauth.google.GoogleProfile;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoProfile;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoAccessToken;
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

    public TokenResponse authenticateOfKakao(String code) {
        KakaoAccessToken kakaoAccessToken = kakaoOAuth2Service.requestToken(code);
        KakaoProfile kaKaoProfile = kakaoOAuth2Service.requestProfile(kakaoAccessToken);

        String email = kaKaoProfile.getKakao_account().getEmail();
        String nickname = kaKaoProfile.getKakao_account().getProfile().getNickname();

        User user = userRepository.findByEmail(email).orElseGet(() -> joinUser(email, nickname, Provider.KAKAO));
        SecurityUser securityUser = convertSecurityUser(user);

        TokenResponse newToken = jwtTokenProvider.generateToken(securityUser);
        redisRefreshTokenService.save(securityUser.getId(), newToken.refreshToken());

        return newToken;
    }

    public TokenResponse authenticateOfGoogle(String code) {
        GoogleAccessToken googleAccessToken = googleOAuth2Service.requestToken(code);
        GoogleProfile googleProfile = googleOAuth2Service.requestProfile(googleAccessToken);

        String email = googleProfile.getEmail();
        String id = googleProfile.getId();

        User user = userRepository.findByEmail(email).orElseGet(() -> joinUser(email, id, Provider.GOOGLE));
        SecurityUser securityUser = convertSecurityUser(user);

        TokenResponse newToken = jwtTokenProvider.generateToken(securityUser);
        redisRefreshTokenService.save(securityUser.getId(), newToken.refreshToken());

        return newToken;
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
