package com.example.UserManagementService.user.login.service.login;

import com.example.UserManagementService.user.dto.LoginRequest;
import com.example.UserManagementService.user.dto.TokenDTO;

public interface LoginServiceInterface {
    public TokenDTO login(LoginRequest loginRequest);
}