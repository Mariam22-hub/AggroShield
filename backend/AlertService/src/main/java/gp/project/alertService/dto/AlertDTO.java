package gp.project.alertService.dto;

import com.example.shared.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {

    private String eventType;
    private String description;
    private String driverFullName;
    private UserDTO userDTO;

    @Override
    public String toString() {
        return "AlertDTO{" +
                "Driver Name='" + driverFullName + '\'' +
//                ", Vehicle Number='" + vehicleNumber + '\'' +
                ", eventType='" + eventType + '\'' +
//                ", Date=" + timestamp +
                ", Description='" + description + '\'' +
                '}';
    }
}