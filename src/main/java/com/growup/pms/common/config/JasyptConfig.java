package com.growup.pms.common.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import java.util.Objects;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {
    // https://github.com/ulisesbocchio/jasypt-spring-boot?tab=readme-ov-file#password-based-encryption-configuration
    private static final String PASSWORD;
    private static final String ALGORITHM = "PBEWITHHMACSHA512ANDAES_256";
    private static final String KEY_OBJECTION_ITERATIONS = "1000";
    private static final String POOL_SIZE = "1";
    private static final String PROVIDER_NAME = "SunJCE";
    private static final String SALT_GENERATOR_CLASS_NAME = "org.jasypt.salt.RandomSaltGenerator";
    private static final String IV_GENERATOR_CLASS_NAME = "org.jasypt.iv.RandomIvGenerator";
    private static final String STRING_OUTPUT_TYPE = "base64";

    static {
        PASSWORD = Objects.requireNonNull(System.getenv("JASYPT_PASSWORD"), "JASYPT_PASSWORD 환경 변수가 설정되지 않았습니다.");
    }

    @Bean("jasyptEncryptorAES")
    public StringEncryptor stringEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(PASSWORD);
        config.setAlgorithm(ALGORITHM);
        config.setKeyObtentionIterations(KEY_OBJECTION_ITERATIONS);
        config.setPoolSize(POOL_SIZE);
        config.setProviderName(PROVIDER_NAME);
        config.setSaltGeneratorClassName(SALT_GENERATOR_CLASS_NAME);
        config.setIvGeneratorClassName(IV_GENERATOR_CLASS_NAME);
        config.setStringOutputType(STRING_OUTPUT_TYPE);

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }
}
