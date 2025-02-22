package com.example.readinggenerationservice.service.prediction;

import com.example.readinggenerationservice.model.SensorReadings;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PredictionService implements PredictionServiceInterface{
    private final RestTemplate restTemplate;

    // Inject the RestTemplate bean
    public PredictionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getPrediction(SensorReadings input) {
        try {
            String url = "http://localhost:8000/predict";

            return restTemplate.postForObject(url, input, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

    }
}