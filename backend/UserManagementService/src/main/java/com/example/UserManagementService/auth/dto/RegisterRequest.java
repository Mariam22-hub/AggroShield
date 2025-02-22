package com.example.UserManagementService.auth.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest

{
    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String firstName;

    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String lastName;

    @NotEmpty(message = "Username is mandatory")
    @NotNull(message = "Username is mandatory")
    private String Username;

    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;

    @NotEmpty(message = "phone number is mandatory")
    @NotNull(message = "phone number is mandatory")
    private String phoneNumber;

    @Column(nullable = false)
    @NotBlank(message = "car model is required")
    private String carModel;

    @Column(nullable = false)
    @NotBlank(message = "vehicle number is required")
    private String vehicleNumber;
}