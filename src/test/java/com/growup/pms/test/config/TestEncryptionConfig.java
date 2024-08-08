package com.growup.pms.test.config;

import com.growup.pms.common.util.EncryptionUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestEncryptionConfig {

    @Bean
    public EncryptionUtil encryptionUtil() {
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        encryptionUtil.init();
        return encryptionUtil;
    }
}
