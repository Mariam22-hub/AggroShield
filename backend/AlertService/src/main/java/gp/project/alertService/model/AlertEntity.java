package gp.project.alertService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "alert")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AlertEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "event name is required")
    @Column(name = "aggressive_event", nullable = false)
    private String eventType = "Aggressive Driving";

    @NotBlank(message = "description is required")
    @Column(name = "description", nullable = false)
    private String Description;


    // User Details
    @Email
    @NotBlank(message = "Email is required")
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @NotBlank(message = "user full name is required")
    @Column(name = "user_full_name", nullable = false)
    private String userFullName;

    @NotBlank(message = "vehicle number is required")
    @Column(name = "user_vehicle_number", nullable = false)
    private String vehicleNumber;

    @NotBlank(message = "car model is required")
    @Column(name = "user_car_model", nullable = false)
    private String carModel;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = true, insertable = false)
    private LocalDateTime lastModifiedAt;
}