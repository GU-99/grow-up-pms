package com.growup.pms.common.util;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DecryptionException;
import com.growup.pms.common.exception.exceptions.EncryptionException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static byte[] key;

    @Value("${encryption.key}")
    public void generateKey(String key) {
        if (key == null || key.length() != 16) {
            throw new IllegalArgumentException("잘못된 키 입니다.");
        }
        EncryptionUtil.key = key.getBytes();
    }

    public static String encrypt(String plainText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new EncryptionException(ErrorCode.ENCRYPTION_ERROR);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new DecryptionException(ErrorCode.DECRYPTION_ERROR);
        }
    }
}
