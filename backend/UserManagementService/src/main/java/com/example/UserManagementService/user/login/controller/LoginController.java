package com.example.UserManagementService.user.login.controller;

import com.example.UserManagementService.DTO.ApiResponse;
import com.example.UserManagementService.user.dto.LoginRequest;
import com.example.UserManagementService.user.dto.TokenDTO;
import com.example.UserManagementService.user.login.service.login.LoginService;
import com.example.UserManagementService.user.login.service.login.LoginServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginServiceInterface loginService;
    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenDTO tokenDTO =loginService.login(loginRequest);
        return new ApiResponse<>(tokenDTO);
    }
}