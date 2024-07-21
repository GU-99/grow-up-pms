package com.growup.pms.test.support;

import com.growup.pms.test.annotation.DatabaseCleanupTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

@DatabaseCleanupTest
public abstract class AcceptanceTestSupport {
    @LocalServerPort
    private int port;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }
}
