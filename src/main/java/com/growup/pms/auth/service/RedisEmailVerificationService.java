package com.growup.pms.auth.service;

import com.growup.pms.auth.service.dto.EmailSendCommand;
import com.growup.pms.auth.service.mail.MailClient;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RedisEmailVerificationService implements EmailVerificationService {
    private static final String KEYSPACE_USER_EMAIL_CODE = "user:%s:email";
    private static final Duration VERIFICATION_CODE_EXPIRATION = Duration.ofSeconds(60);
    private static final int MAX_VERIFICATION_CODE = 999999;

    private final MailClient mailClient;
    private final StringRedisTemplate stringRedisTemplate;

    public boolean verifyAndInvalidateEmail(String email, String code) {
        String redisKey = KEYSPACE_USER_EMAIL_CODE.formatted(email);
        String storedCode = stringRedisTemplate.opsForValue().get(redisKey);

        if (StringUtils.hasLength(storedCode) && storedCode.equals(code)) {
            stringRedisTemplate.delete(redisKey);
            return true;
        }
        return false;
    }

    public void sendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();

        stringRedisTemplate.opsForValue().set(KEYSPACE_USER_EMAIL_CODE.formatted(email), verificationCode,
                VERIFICATION_CODE_EXPIRATION);

        mailClient.sendEmail(EmailSendCommand.builder()
                .recipient(email)
                .subject("인증 코드 안내 - [서비스 이름]")
                .content("""
                    안녕하세요,
                    
                    아래의 인증 코드를 사용하여 이메일 주소를 인증해 주세요:
                    
                    인증 코드: %s
                    
                    이 코드는 %d초 후에 만료됩니다.
                    """.formatted(verificationCode, VERIFICATION_CODE_EXPIRATION.getSeconds()))
                .build());
    }

    private String generateVerificationCode() {
        return "%06d".formatted(ThreadLocalRandom.current().nextInt(MAX_VERIFICATION_CODE));
    }
}
