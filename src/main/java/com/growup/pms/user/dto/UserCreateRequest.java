package com.growup.pms.user.dto;

import com.growup.pms.common.validation.constraint.FieldMatch;
import com.growup.pms.user.domain.Provider;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.domain.UserProfile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldMatch(first = "password", second = "passwordConfirm")
public class UserCreateRequest {
    @Email
    private String username;

    @Length(min = 8, max = 20)
    private String password;

    private String passwordConfirm;

    @NotBlank
    @Length(max = 20)
    private String nickname;

    private String content;

    private String profileImage;

    @Builder
    public UserCreateRequest(String username, String password, String passwordConfirm, String nickname, String content,
                             String profileImage) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.nickname = nickname;
        this.content = content;
        this.profileImage = profileImage;
    }

    public static User toEntity(UserCreateRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .provider(Provider.LOCAL)
                .profile(UserProfile.builder()
                        .nickname(request.getNickname())
                        .content(request.getContent())
                        .profileImage(request.getProfileImage())
                        .build())
                .build();
    }
}
