package com.growup.pms.auth.service;

public interface EmailVerificationService {
    boolean verifyAndInvalidateEmail(String email, String code);

    void sendVerificationCode(String email);
}
