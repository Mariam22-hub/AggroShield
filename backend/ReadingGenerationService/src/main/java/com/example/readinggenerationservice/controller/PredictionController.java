package com.example.readinggenerationservice.controller;

import com.example.readinggenerationservice.model.SensorReadings;
import com.example.readinggenerationservice.service.prediction.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prediction")
public class PredictionController  {
    private final PredictionService predictionService;

    @Autowired
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/predict")
    @CrossOrigin(origins = "*")
    public String predict(@RequestBody SensorReadings input) {
        // Call the PredictionService and return the response
        return predictionService.getPrediction(input);
    }
}