package com.growup.pms.auth.service.mail;

public interface EmailVerificationService {

    boolean verifyAndInvalidateEmail(String email, String code);

    void sendVerificationCode(String email);
}
