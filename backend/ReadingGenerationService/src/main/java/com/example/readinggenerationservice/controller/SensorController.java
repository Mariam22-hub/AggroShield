package com.example.readinggenerationservice.controller;
//import com.example.readinggenerationservice.dto.UserDTO;
import com.example.readinggenerationservice.dto.SensorReportDataDTO;
import com.example.readinggenerationservice.entity.SensorReadingsEntity;
import com.example.readinggenerationservice.repository.SensorReadingsRepository;
import com.example.readinggenerationservice.service.prediction.PredictionService;
import com.example.readinggenerationservice.service.sensor.SensorServiceInterface;
import com.example.shared.DTO.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.readinggenerationservice.model.SensorReadings;
import com.example.readinggenerationservice.service.sensor.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@Slf4j
@RequestMapping("/api/sensor")
public class SensorController {
    private final SensorServiceInterface sensorService;
    private final PredictionService predictionService;
    private final SensorReadingsRepository sensorReadingsRepository;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;

    Logger logger = LoggerFactory.getLogger(SensorController.class);

    public SensorController(SensorService sensorService, PredictionService predictionService,
                            SensorReadingsRepository sensorReadingsRepository) {
        this.sensorService = sensorService;
        this.predictionService = predictionService;
        this.sensorReadingsRepository = sensorReadingsRepository;
    }


    @PostMapping("/start/{carNumber}")
    public String startGeneratingReadings(@PathVariable String carNumber) {
        UserDTO userDTO = sensorService.getCurrentUserDto();

        if (running) return "Generation already running!";
        running = true;
        executorService.submit(() -> {

            while (running) {
                SensorReadings reading = sensorService.generateSensorReading(carNumber, userDTO);
                logger.info("Generated and saved reading: {}", reading);

                String prediction = predictionService.getPrediction(reading);
                System.out.println("Prediction: " + prediction);

                if ("Aggressive".equalsIgnoreCase(prediction)) {
                    sensorService.sendAlertToAlertService(carNumber);
                }
                try {
                    Thread.sleep(5000); // Delay between readings
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        return "Started generating sensor readings for car: " + carNumber;
    }

    @PostMapping("/stop")
    public String stopGeneratingReadings() {
        running = false;
        return "Stopped generating sensor readings!";
    }

    @GetMapping("/readings")
    public List<SensorReadingsEntity> getReadingsByCar() {
        return sensorReadingsRepository.findAll();
    }

    @GetMapping("/readings/{carNumber}")
    public List<SensorReadingsEntity> getReadingsByCar(@PathVariable String carNumber) {
        return sensorReadingsRepository.findByCarNumber(carNumber);
    }

    @PostMapping("/test_prediction")
    public ResponseEntity<String> testPrediction(@RequestBody SensorReadings reading) {
        logger.info("Received reading for testing: {}", reading);

        // Get the prediction
        String prediction = predictionService.getPrediction(reading);
        logger.info("Prediction for test input: {}", prediction);

        String userFullName = "";

        // Check if the prediction is aggressive
        if ("{\"prediction\":\"AGGRESSIVE\"}".equals(prediction)) {
            userFullName = sensorService.sendAlertToAlertService(reading.getCarNumber());
        }
        return ResponseEntity.ok("Prediction: Aggressive. Alert sent for user: " + userFullName);
    }

    @GetMapping("/readings/data")
    public List<SensorReportDataDTO> getSensorReadingsData() {
        List<SensorReadingsEntity> readings = sensorReadingsRepository.findAll();

        return readings.stream()
                .map(reading -> new SensorReportDataDTO(
                        reading.getAccX(),
                        reading.getAccY(),
                        reading.getAccZ(),
                        reading.getGyroX(),
                        reading.getGyroY(),
                        reading.getGyroZ(),
                        reading.getTimestamp(), // Format timestamp as a string
                        reading.getPrediction()
                ))
                .collect(Collectors.toList()); // Explicitly collect to a List
    }

}