package com.growup.pms.common.util;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.InternalServerException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public interface HashingUtil {
    String DEFAULT_HASHING_ALGORITHM = "SHA-256";

    static String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(DEFAULT_HASHING_ALGORITHM);
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
