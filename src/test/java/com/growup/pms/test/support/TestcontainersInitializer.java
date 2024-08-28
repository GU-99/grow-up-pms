package com.growup.pms.test.support;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.lifecycle.Startables;

public class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:11.5.2")
            .withReuse(true);

    static {
        Startables.deepStart(mariadb).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + mariadb.getJdbcUrl(),
                "spring.datasource.username=" + mariadb.getUsername(),
                "spring.datasource.password=" + mariadb.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
