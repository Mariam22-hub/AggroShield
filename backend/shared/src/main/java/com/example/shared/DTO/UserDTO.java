package com.example.shared.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String fullName;
    private String email;
    private String vehicleNumber;
    private String carModel;
}