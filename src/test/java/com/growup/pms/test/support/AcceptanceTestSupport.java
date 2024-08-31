package com.growup.pms.test.support;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import com.growup.pms.test.annotation.ContainerTest;
import com.growup.pms.test.annotation.DatabaseCleanupTest;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

@ContainerTest
@DatabaseCleanupTest
public abstract class AcceptanceTestSupport {
    @LocalServerPort
    private int port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }

    protected void 사용자가_새로_가입한다(User 새_사용자) {
        새_사용자.encodePassword(passwordEncoder);
        userRepository.save(새_사용자);
    }

    protected TokenResponse 인증_정보를_생성한다(User 사용자) {
        return jwtTokenProvider.generateToken(SecurityUser.builder()
                .username(사용자.getUsername())
                .password(사용자.getPassword())
                .build());
    }
}
