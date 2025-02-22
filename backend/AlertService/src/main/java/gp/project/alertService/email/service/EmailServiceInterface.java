package gp.project.alertService.email.service;

import gp.project.alertService.email.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailServiceInterface {
    public void sendAlertEmail(String to, String fullName, String vehicleNumber,
                              EmailTemplateName emailTemplate) throws MessagingException;
}