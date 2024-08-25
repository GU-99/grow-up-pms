package com.growup.pms.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.service.dto.EmailDetails;
import com.growup.pms.auth.service.mail.MailClient;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import java.time.Duration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {
    @Mock
    MailClient mailClient;

    @Mock
    StringRedisTemplate stringRedisTemplate;

    @Mock
    ValueOperations<String, String> valueOperations;

    @InjectMocks
    EmailVerificationService emailVerificationService;

    @Nested
    class 이메일을_인증할_때 {
        @Test
        void 성공한다() {
            // given
            String email = "exists@email.com";
            String code = "123456";
            String key = "user:%s:email".formatted(email);

            when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
            when(valueOperations.get(anyString())).thenReturn(code);

            // when
            boolean isVerified = emailVerificationService.verifyAndInvalidateEmail(email, code);

            // then
            assertThat(isVerified).isTrue();

            verify(stringRedisTemplate).delete(key);
        }

        @Test
        void 저장된_인증_코드가_없으면_인증에_실패한다() {
            // given
            String 이메일 = "not_exists@email.com";
            String 코드 = "123456";

            when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
            when(valueOperations.get(anyString())).thenReturn(null);

            // when
            boolean 인증_결과 = emailVerificationService.verifyAndInvalidateEmail(이메일, 코드);

            // then
            assertThat(인증_결과).isFalse();
        }

        @Test
        void 인증_코드가_일치하지_않으면_인증에_실패한다() {
            // given
            String 메일 = "not_exists@email.com";
            String 코드 = "123456";
            String 실제_코드 = "654321";

            when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
            when(valueOperations.get(anyString())).thenReturn(실제_코드);

            // when
            boolean 인증_결과 = emailVerificationService.verifyAndInvalidateEmail(메일, 코드);

            // then
            assertThat(인증_결과).isFalse();
        }
    }

    @Nested
    class 인증_이메일을_보낼_때 {
        @Test
        void 성공한다() {
            // given
            String 메일 = "exists@email.com";

            when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
            doNothing().when(valueOperations).set(anyString(), anyString(), any(Duration.class));
            doNothing().when(mailClient).sendEmail(any(EmailDetails.class));

            // when
            assertThatCode(() -> emailVerificationService.sendVerificationCode(메일))
                    .doesNotThrowAnyException();
        }
    }
}
