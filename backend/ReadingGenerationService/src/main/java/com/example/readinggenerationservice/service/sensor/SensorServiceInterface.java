package com.example.readinggenerationservice.service.sensor;

//import com.example.readinggenerationservice.dto.UserDTO;
import com.example.readinggenerationservice.model.SensorReadings;
import com.example.shared.DTO.UserDTO;

public interface SensorServiceInterface {
    public SensorReadings generateSensorReading(String carNumber, UserDTO userDTO);
    public String sendAlertToAlertService(String carNumber);
    public UserDTO getCurrentUserDto();
}