package com.growup.pms.auth.service.mail;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.auth.service.dto.EmailSendCommand;
import com.growup.pms.auth.service.mail.client.MailtrapClient;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MailtrapClientTest {
    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    MailtrapClient mailtrapClient;

    static final String 보내는_사람_이메일 = "sandbox@mailtrap.io";

    @Test
    void 이메일을_보내는_데_성공한다() {
        // given
        EmailSendCommand 메일 = EmailSendCommand.builder()
                .subject("제목")
                .content("내용")
                .recipient("recipient@example.org")
                .build();
        MimeMessage 메시지 = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(메시지);
        doNothing().when(javaMailSender).send(메시지);

        ReflectionTestUtils.setField(mailtrapClient, "sandboxSenderEmail", 보내는_사람_이메일);

        // when & then
        assertThatCode(() -> mailtrapClient.sendEmail(메일))
                .doesNotThrowAnyException();
    }

    @Test
    void 수신자가_없으면_예외가_발생한다() {
        // given
        EmailSendCommand 메일 = EmailSendCommand.builder()
                .subject("제목")
                .content("내용")
                .recipient(null)
                .build();
        MimeMessage 메시지 = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(메시지);

        ReflectionTestUtils.setField(mailtrapClient, "sandboxSenderEmail", 보내는_사람_이메일);

        // when & then
        assertThatThrownBy(() -> mailtrapClient.sendEmail(메일))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("To address must not be null");
    }
}
