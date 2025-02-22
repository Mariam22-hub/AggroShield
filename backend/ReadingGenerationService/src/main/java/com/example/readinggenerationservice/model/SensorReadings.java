package com.example.readinggenerationservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SensorReadings {
    @JsonProperty("CarNumber")
    private String carNumber;

    @JsonProperty("AccX")
    private double accX;

    @JsonProperty("AccY")
    private double accY;

    @JsonProperty("AccZ")
    private double accZ;

    @JsonProperty("GyroX")
    private double gyroX;

    @JsonProperty("GyroY")
    private double gyroY;

    @JsonProperty("GyroZ")
    private double gyroZ;


    public SensorReadings() {
        // No-arg constructor for deserialization
    }


    public SensorReadings(String carNumber,double accX, double accY, double accZ, double gyroX, double gyroY, double gyroZ) {

        this.carNumber = carNumber;
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;

    }

    @Override
    public String toString() {
        return
                "CarNumber=" + carNumber +
                "AccX=" + accX +
                ", AccY=" + accY +
                ", AccZ=" + accZ +
                ", GyroX=" + gyroX +
                ", GyroY=" + gyroY +
                ", GyroZ=" + gyroZ
                ;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public double getAccX() {
        return accX;
    }

    public double getAccY() {
        return accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public double getGyroX() {
        return gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }
}
