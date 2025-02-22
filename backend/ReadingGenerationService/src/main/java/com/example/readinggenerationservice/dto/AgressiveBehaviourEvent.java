package com.example.readinggenerationservice.dto;
import com.example.shared.DTO.UserDTO;

import java.time.LocalDateTime;

public record AgressiveBehaviourEvent(
        String eventType,
//        LocalDateTime timestamp,
        String description,
        String driverFullName,
        UserDTO userDTO
) {
}