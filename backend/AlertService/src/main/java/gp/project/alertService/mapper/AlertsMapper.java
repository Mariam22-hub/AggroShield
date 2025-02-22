package gp.project.alertService.mapper;

import com.example.shared.DTO.UserDTO;
import gp.project.alertService.dto.AlertDTO;
import gp.project.alertService.model.AlertEntity;


public class AlertsMapper {
    public static AlertEntity mapToAlertsEntity(AlertEntity alertEntity, AlertDTO alertDTO) {
        alertEntity.setEventType(alertDTO.getEventType());
        alertEntity.setDescription(alertDTO.getDescription());

        alertEntity.setUserEmail(alertDTO.getUserDTO().getEmail());
        alertEntity.setUserFullName(alertDTO.getUserDTO().getFullName());
        alertEntity.setVehicleNumber(alertDTO.getUserDTO().getVehicleNumber());
        alertEntity.setCarModel(alertDTO.getUserDTO().getCarModel());

        return alertEntity;
    }

    public static AlertDTO createAlertDTO(String description, String email, String fullName, String vehicleNumber, String carModel) {
        AlertDTO alertDTO = new AlertDTO();
        alertDTO.setDescription(description);
        alertDTO.setEventType("Aggressive Driving");
        UserDTO userDTO = createUserDTO(email, fullName, vehicleNumber, carModel);
        alertDTO.setUserDTO(userDTO);

        return alertDTO;
    }

    private static UserDTO createUserDTO(String email, String fullName, String vehicleNumber, String carModel) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setFullName(fullName);
        userDTO.setVehicleNumber(vehicleNumber);
        userDTO.setCarModel(carModel);
        return userDTO;
    }
}