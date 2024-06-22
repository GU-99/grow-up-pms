package com.growup.pms.user.domain;

import com.growup.pms.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

    @Column(nullable = false)
    private String username;

    private String password;

    @Embedded
    private UserProfile profile;

    @Column(nullable = false)
    private LocalDateTime passwordChangeDate;

    private int passwordFailureCount;

    @Builder
    public User(String username, String password, UserProfile profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
        this.passwordFailureCount = 0;
        this.passwordChangeDate = LocalDateTime.now();
    }

    public void increasePasswordFailureCount() {
        passwordFailureCount++;
    }

    public void changePassword(PasswordEncoder passwordEncoder, String newPassword) {
        password = passwordEncoder.encode(newPassword);
        passwordChangeDate = LocalDateTime.now();
    }
}
