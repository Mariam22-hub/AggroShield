package com.example.UserManagementService.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotEmpty(message = "email shouldnt be empty")
    @jakarta.validation.constraints.Email(message = "email is not well formatted")
    @NotBlank
    private String Email;

    @Size(min = 8, message = "password must be atleast 8 chars")
    @NotBlank
    private String Password;
}