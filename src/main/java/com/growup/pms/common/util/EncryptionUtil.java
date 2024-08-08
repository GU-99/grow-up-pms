package com.growup.pms.common.util;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DecryptionException;
import com.growup.pms.common.exception.exceptions.EncryptionException;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String PADDING = "/ECB/PKCS5Padding";

    @Value("${encryption.secret-key}")
    private String secretKey;

    private static String SECRET;

    @PostConstruct
    public void init() {
        SECRET = secretKey;
    }

    private static SecretKey generateKey() {
        byte[] keyBytes = SECRET.getBytes();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM + PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getUrlEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("Encryption error: ", e);
            throw new EncryptionException(ErrorCode.ENCRYPTION_ERROR);
        }
    }

    public static Long decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM + PADDING);
            cipher.init(Cipher.DECRYPT_MODE, generateKey());
            byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedString = new String(decryptedBytes);
            return Long.parseLong(decryptedString);
        } catch (Exception e) {
            log.error("Decryption error: ", e);
            throw new DecryptionException(ErrorCode.DECRYPTION_ERROR);
        }
    }
}
