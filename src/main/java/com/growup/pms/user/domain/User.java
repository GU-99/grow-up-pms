package com.growup.pms.user.domain;

import com.growup.pms.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Embedded
    private UserProfile profile;

    @Column(nullable = false)
    private LocalDateTime passwordChangeDate;

    @Column(nullable = false)
    private int passwordFailureCount;

    @Builder
    public User(String email, String password, Provider provider, UserProfile profile) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.profile = profile;
        this.passwordFailureCount = 0;
        this.passwordChangeDate = LocalDateTime.now();
    }

    public void increasePasswordFailureCount() {
        passwordFailureCount++;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void changePassword(PasswordEncoder passwordEncoder, String newPassword) {
        password = passwordEncoder.encode(newPassword);
        passwordChangeDate = LocalDateTime.now();
    }

    public void updateImage(String image) {
        this.profile.setImage(image);
    }

    public void updateImageName(String imageName) {
        this.profile.setImageName(imageName);
    }
}
