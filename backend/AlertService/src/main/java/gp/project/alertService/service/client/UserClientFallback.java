package gp.project.alertService.service.client;

import com.example.shared.DTO.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserDTO getCurrentUser() {
        return null;
    }
}