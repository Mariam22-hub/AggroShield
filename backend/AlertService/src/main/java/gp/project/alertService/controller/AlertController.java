package gp.project.alertService.controller;

import com.example.shared.DTO.UserDTO;
import gp.project.alertService.model.AlertEntity;
import gp.project.alertService.respository.AlertRepository;
import gp.project.alertService.service.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/alerts")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AlertController {
    private final AlertRepository alertRepository;
    @Qualifier("gp.project.alertService.service.client.UserClient")
    private final UserClient userClient;


    @GetMapping("/user")
    public ResponseEntity<List<AlertEntity>> getUserAlerts() {
        // Retrieve the authenticated user's details
        var userDetails = userClient.getCurrentUser();
        String userEmail = userDetails.getEmail();

        log.info("Fetching alerts for user: {}", userEmail);

        List<AlertEntity> alerts = alertRepository.findByUserEmail(userEmail);

        if (alerts.isEmpty()) {
            log.info("No alerts found for user: {}", userEmail);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<AlertEntity>> getAllUsersAlerts() {
        log.info("Fetching all alerts for admin");
        List<AlertEntity> alerts = alertRepository.findAll();
        if (alerts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(alerts);
    }

    /**
     * Retrieve a single alert by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlertEntity> getAlertById(@PathVariable Long id) {
        log.info("Fetching alert details for ID: {}", id);

        Optional<AlertEntity> alertOptional = alertRepository.findById(id);

        if (alertOptional.isPresent()) {
            return ResponseEntity.ok(alertOptional.get());
        } else {
            log.warn("Alert not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}