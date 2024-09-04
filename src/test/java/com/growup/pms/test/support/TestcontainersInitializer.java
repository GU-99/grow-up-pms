package com.growup.pms.test.support;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.lifecycle.Startables;

public class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static final MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:11.5.2")
            .withReuse(true);

    static final RedisContainer redis = new RedisContainer("redis:7.4.0-alpine")
            .withReuse(true);

    static {
        Startables.deepStart(mariadb, redis).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + mariadb.getJdbcUrl(),
                "spring.datasource.username=" + mariadb.getUsername(),
                "spring.datasource.password=" + mariadb.getPassword(),
                "spring.data.redis.host=" + redis.getHost(),
                "spring.data.redis.port=" + redis.getFirstMappedPort()
        ).applyTo(applicationContext.getEnvironment());
    }
}
