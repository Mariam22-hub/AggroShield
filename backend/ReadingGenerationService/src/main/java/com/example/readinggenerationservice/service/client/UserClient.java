package com.example.readinggenerationservice.service.client;

//import com.example.readinggenerationservice.dto.UserDTO;
import com.example.shared.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "api-gateway", fallback = UserClientFallback.class, url = "http://localhost:8090") // Update the URL for UserManagementService
public interface UserClient {
    @GetMapping(value = "/users/me", headers = "Accept=application/json")
    UserDTO getCurrentUser();
}