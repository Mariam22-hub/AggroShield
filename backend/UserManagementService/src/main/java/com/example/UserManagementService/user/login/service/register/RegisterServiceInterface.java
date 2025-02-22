package com.example.UserManagementService.user.login.service.register;

import com.example.UserManagementService.auth.dto.RegisterRequest;
import com.example.UserManagementService.user.dto.TokenDTO;
import jakarta.mail.MessagingException;

public interface RegisterServiceInterface {
    public TokenDTO register(RegisterRequest registerRequest) throws MessagingException;
    public void activateAccount(String incoming_code) throws MessagingException;
}