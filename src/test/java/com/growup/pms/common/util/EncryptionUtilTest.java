package com.growup.pms.common.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
class EncryptionUtilTest {

    @Autowired
    EncryptionUtil encryptionUtil;

    @Test
    void 암복호화가_성공한다() {
        String plainText = "1";
        String encrypted = EncryptionUtil.encrypt(plainText);

        Long decrypted = EncryptionUtil.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(Long.parseLong(plainText));
    }
}
