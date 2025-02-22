// RegisterController.java
package com.example.UserManagementService.user.login.controller;

import com.example.UserManagementService.DTO.ApiResponse;
import com.example.UserManagementService.user.dto.TokenDTO;
import com.example.UserManagementService.auth.dto.RegisterRequest;

import com.example.UserManagementService.user.login.service.register.RegisterService;
import com.example.UserManagementService.user.login.service.register.RegisterServiceInterface;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterServiceInterface registerService;

    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        TokenDTO tokenDTO = registerService.register(registerRequest);
        return new ApiResponse<>(tokenDTO);
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String activationCode
    ) throws MessagingException {
        registerService.activateAccount(activationCode);
    }
}