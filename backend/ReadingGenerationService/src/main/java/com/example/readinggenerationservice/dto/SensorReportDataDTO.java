package com.example.readinggenerationservice.dto;

import java.time.LocalDateTime;

public class SensorReportDataDTO {
    private double accX;
    private double accY;
    private double accZ;
    private double gyroX;
    private String prediction;

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public SensorReportDataDTO(){

    }

    public SensorReportDataDTO(double accX, double accY, double accZ,
                               double gyroX, double gyroY, double gyroZ,
                               LocalDateTime timestamp, String prediction) {
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;
        this.timestamp = timestamp;
        this.prediction = prediction;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public double getGyroX() {
        return gyroX;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    private double gyroY;
    private double gyroZ;
    private LocalDateTime timestamp; // Use String for easy formatting in the report
}