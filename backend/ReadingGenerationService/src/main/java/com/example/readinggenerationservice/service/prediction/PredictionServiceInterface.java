package com.example.readinggenerationservice.service.prediction;

import com.example.readinggenerationservice.model.SensorReadings;

public interface PredictionServiceInterface {
    public String getPrediction(SensorReadings input);
}