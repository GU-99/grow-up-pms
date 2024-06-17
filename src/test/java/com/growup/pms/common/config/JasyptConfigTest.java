package com.growup.pms.common.config;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {JasyptConfig.class})
class JasyptConfigTest {

    private final StringEncryptor stringEncryptor;

    public JasyptConfigTest(@Qualifier("jasyptEncryptorAES") StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }


    @Test
    @DisplayName("Jasypt를 활용한 암호화를 확인한다.")
    public void Encrypt(){
        //given
        String password = System.getenv("JASYPT_PASSWORD");
        String plainText = "grow-up-pms";

        //when
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword(password);

        //then
        String encryptText = stringEncryptor.encrypt(plainText);
        String decryptText = this.stringEncryptor.decrypt(encryptText);

        Assertions.assertThat(decryptText).isEqualTo(plainText);
    }

}
