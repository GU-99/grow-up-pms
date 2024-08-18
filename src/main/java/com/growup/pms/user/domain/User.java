package com.growup.pms.user.domain;

import com.growup.pms.common.BaseEntity;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @Column(nullable = false)
    private String email;

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
    public User(String username, String password, String email, Provider provider, UserProfile profile) {
        this.username = username;
        this.password = password;
        this.email = email;
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
}
