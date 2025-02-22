package com.example.UserManagementService.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@Validated
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password can't be less than 8")
    private String password;

    @NotBlank(message = "Role is required") // Optional validation
    private String role;

}
