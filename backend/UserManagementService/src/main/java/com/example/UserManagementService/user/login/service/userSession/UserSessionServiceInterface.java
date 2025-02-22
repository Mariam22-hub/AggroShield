package com.example.UserManagementService.user.login.service.userSession;

import com.example.UserManagementService.DTO.UserSession;

import java.util.Optional;

public interface UserSessionServiceInterface {
    public Optional<UserSession> getUserSession(String userId);
    public void saveUserSession(UserSession userSession);
}