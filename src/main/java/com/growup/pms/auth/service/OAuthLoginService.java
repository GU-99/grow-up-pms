package com.growup.pms.auth.service;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.auth.service.dto.oauth.google.GoogleAccessToken;
import com.growup.pms.auth.service.dto.oauth.google.GoogleProfile;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoProfile;
import com.growup.pms.auth.service.dto.oauth.kakao.KakaoAccessToken;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.common.util.GoogleUtil;
import com.growup.pms.common.util.KakaoUtil;
import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import com.growup.pms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final KakaoUtil kakaoUtil;
    private final GoogleUtil googleUtil;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService redisRefreshTokenService;

    public TokenResponse authenticateOfKakao(String code) {
        KakaoAccessToken kakaoAccessToken = kakaoUtil.requestToken(code);
        KakaoProfile kaKaoProfile = kakaoUtil.requestProfile(kakaoAccessToken);

        String email = kaKaoProfile.getKakao_account().getEmail();
        String nickname = kaKaoProfile.getKakao_account().getProfile().getNickname();

        User user = userRepository.findByEmail(email).orElseGet(() -> joinUser(email, nickname, Provider.KAKAO));
        SecurityUser securityUser = convertSecurityUser(user);

        TokenResponse newToken = jwtTokenProvider.generateToken(securityUser);
        redisRefreshTokenService.save(securityUser.getId(), newToken.refreshToken());

        return newToken;
    }

    public TokenResponse authenticateOfGoogle(String code) {
        GoogleAccessToken googleAccessToken = googleUtil.requestToken(code);
        GoogleProfile googleProfile = googleUtil.requestProfile(googleAccessToken);

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
                .password(null)
                .build();
    }

    private User joinUser(String email, String nickname, Provider provider) {
        User user = User.builder()
                .provider(provider)
                .email(email)
                .username(email)
                .profile(UserProfile.builder()
                        .nickname(nickname)
                        .bio(null)
                        .image(null)
                        .imageName(null)
                        .build())
                .password(null)
                .links(null)
                .build();

        return userRepository.save(user);
    }
}
