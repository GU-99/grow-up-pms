package com.growup.pms.user.dto;

import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreateDto {
    private String email;
    private String password;
    private String nickname;
    private String bio;
    private String image;

    @Builder
    public UserCreateDto(String email, String password, String nickname, String bio, String image) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.bio = bio;
        this.image = image;
    }

    public static User toEntity(UserCreateDto request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .provider(Provider.LOCAL)
                .profile(UserProfile.builder()
                        .nickname(request.getNickname())
                        .bio(request.getBio())
                        .image(request.getImage())
                        .build())
                .build();
    }
}
