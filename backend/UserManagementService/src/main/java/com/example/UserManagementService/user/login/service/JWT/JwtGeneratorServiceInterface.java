package com.example.UserManagementService.user.login.service.JWT;

import com.example.UserManagementService.user.model.User;

public interface JwtGeneratorServiceInterface {
    public String generateToken(User opsUser);
}