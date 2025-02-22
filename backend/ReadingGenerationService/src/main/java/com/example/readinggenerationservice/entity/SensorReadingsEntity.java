package com.example.readinggenerationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data // Lombok annotation to generate getters and setters
@Entity
@Table(name = "sensor_readings")
public class SensorReadingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String carNumber;

    @Column(nullable = false)
    private double accX;

    @Column(nullable = false)
    private double accY;

    @Column(nullable = false)
    private double accZ;

    @Column(nullable = false)
    private double gyroX;

    @Column(nullable = false)
    private double gyroY;

    @Column(nullable = false)
    private double gyroZ;

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

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Column(nullable = false)
    private LocalDateTime timestamp;
    public String getCarNumber() {
        return carNumber;
    }


    public String getPrediction() {
        return Prediction;
    }

    public void setPrediction(String prediction) {
        Prediction = prediction;
    }


    @Column(nullable = false)
    private String Prediction;


    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }

    public double getGyroY() {
        return gyroY;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public double getGyroX() {
        return gyroX;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }
}