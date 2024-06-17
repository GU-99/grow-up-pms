package com.growup.pms.common.config;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import(JasyptConfig.class)
@ExtendWith(SpringExtension.class)
class JasyptConfigTest {

    @Autowired
    @Qualifier("jasyptEncryptorAES")
    private StringEncryptor stringEncryptor;

    @Test
    @DisplayName("Jasypt를 활용한 암호화를 확인한다.")
    void encrypt(){
        // given
        String plainText = "grow-up-pms";

        // when
        String encryptText = stringEncryptor.encrypt(plainText);
        String decryptText = stringEncryptor.decrypt(encryptText);

        // then
        Assertions.assertThat(decryptText).isEqualTo(plainText);
    }

}
