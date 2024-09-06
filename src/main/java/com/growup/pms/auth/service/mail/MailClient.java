package com.growup.pms.auth.service.mail;

import com.growup.pms.auth.service.dto.EmailSendCommand;

public interface MailClient {

    void sendEmail(EmailSendCommand emailDetails);
}
