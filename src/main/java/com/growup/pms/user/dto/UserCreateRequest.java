package com.growup.pms.user.dto;

import com.growup.pms.common.validation.constraint.FieldMatch;
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
    private String email;

    @Length(min = 8, max = 20)
    private String password;

    private String passwordConfirm;

    @NotBlank
    @Length(max = 20)
    private String nickname;

    private String bio;

    private String image;

    @Builder
    public UserCreateRequest(String email, String password, String passwordConfirm, String nickname, String bio,
                             String image) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.nickname = nickname;
        this.bio = bio;
        this.image = image;
    }

    public static UserCreateDto toServiceDto(UserCreateRequest request) {
        return UserCreateDto.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .bio(request.getBio())
                .image(request.getImage())
                .build();
    }
}
