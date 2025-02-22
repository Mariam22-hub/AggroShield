package com.example.UserManagementService.email.service;

import com.example.UserManagementService.email.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailServiceInterface {
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException;
}