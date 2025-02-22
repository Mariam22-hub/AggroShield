package com.example;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    //    private Long id;
    private String userFullName;
    private String email;
    private String phoneNumber;
    private String vehicleNumber;
    private String carModel;
}