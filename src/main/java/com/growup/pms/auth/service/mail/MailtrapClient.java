package com.growup.pms.auth.service.mail;

import com.growup.pms.auth.service.dto.EmailSendCommand;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailtrapClient implements MailClient {

    private final JavaMailSender mailSender;
    private final String sandboxSenderEmail;

    public MailtrapClient(JavaMailSender mailSender, @Value("${mailtrap.from}") String sandboxSenderEmail) {
        this.mailSender = mailSender;
        this.sandboxSenderEmail = sandboxSenderEmail;
    }

    public void sendEmail(EmailSendCommand emailDetails) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(sandboxSenderEmail);
            helper.setTo(emailDetails.recipient());
            helper.setSubject(emailDetails.subject());
            helper.setText(emailDetails.content());

            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            throw new BusinessException(ErrorCode.EMAIL_SENDING_FAILURE);
        }
    }
}
