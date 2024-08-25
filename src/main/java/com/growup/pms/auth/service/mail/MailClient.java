package com.growup.pms.auth.service.mail;

import com.growup.pms.auth.service.dto.EmailDetails;

public interface MailClient {
    void sendEmail(EmailDetails emailDetails);
}
