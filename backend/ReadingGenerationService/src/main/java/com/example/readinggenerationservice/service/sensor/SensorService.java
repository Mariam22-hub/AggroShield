package com.example.readinggenerationservice.service.sensor;

import com.example.readinggenerationservice.dto.AgressiveBehaviourEvent;
//import com.example.readinggenerationservice.dto.UserDTO;
import com.example.readinggenerationservice.entity.SensorReadingsEntity;
import com.example.readinggenerationservice.model.SensorReadings;
import com.example.readinggenerationservice.repository.SensorReadingsRepository;
import com.example.readinggenerationservice.service.client.UserClient;
import com.example.readinggenerationservice.service.prediction.PredictionService;
import com.example.shared.DTO.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class SensorService implements SensorServiceInterface{

    private final SensorReadingsRepository sensorReadingsRepository;
    private final UserClient userClient;
    private final KafkaTemplate<String, AgressiveBehaviourEvent> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(SensorService.class);
    private final PredictionService predictionService;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    public SensorService(SensorReadingsRepository sensorReadingsRepository,
                         @Qualifier("com.example.readinggenerationservice.service.client.UserClient") UserClient userClient, KafkaTemplate<String, AgressiveBehaviourEvent> kafkaTemplate, PredictionService predictionService) {
        this.sensorReadingsRepository = sensorReadingsRepository;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;

        this.predictionService = predictionService;
    }

    @Override
    public UserDTO getCurrentUserDto() {
        return userClient.getCurrentUser();
    }

    public String sendAlertToAlertService(String carNumber) {
        UserDTO userDTO = getCurrentUserDto();
        log.info(String.valueOf(userDTO));

        AgressiveBehaviourEvent event = new AgressiveBehaviourEvent(
                "Aggressive Driving Behavior",
                String.format("Warning: %s, you are exhibiting Aggressive Driving Behavior. Please slow down!",
                        userDTO.getFullName()),
                userDTO.getFullName(),
                userDTO
        );

        try {
            log.info("Before sending alert");
            kafkaTemplate.send(topic,event);
            log.info("Alert sent to Kafka: {}, kafka topic name: {}", event, kafkaTemplate.getDefaultTopic());
        }
        catch (Exception e) {
            log.info(String.valueOf(e));
            log.info(String.valueOf(e.getMessage()));
            e.printStackTrace();
            System.err.println("Failed to send alert to alert service: " + e.getMessage());
        }
        return userDTO.getFullName();
    }

    public SensorReadings generateSensorReading(String carNumber, UserDTO userDTO) {
        // Generate random sensor readings
        SensorReadings reading = new SensorReadings(
                carNumber,
                generateRandomValue(0.1, 1.0),
                generateRandomValue(0.1, 1.0),
                generateRandomValue(0.1, 1.0),
                generateRandomValue(0.1, 1.0),
                generateRandomValue(0.1, 1.0),
                generateRandomValue(0.1, 1.0)
        );

        String prediction = predictionService.getPrediction(reading);

        // Save to database
        saveReadingToDatabase(reading, userDTO,prediction);
        return reading;
    }

    private void saveReadingToDatabase(SensorReadings reading, UserDTO userDTO, String prediction) {
        SensorReadingsEntity entity = new SensorReadingsEntity();
        log.info(String.valueOf(userDTO));

        entity.setCarNumber(reading.getCarNumber());
        entity.setAccX(reading.getAccX());
        entity.setAccY(reading.getAccY());
        entity.setAccZ(reading.getAccZ());
        entity.setGyroX(reading.getGyroX());
        entity.setGyroY(reading.getGyroY());
        entity.setGyroZ(reading.getGyroZ());
        entity.setTimestamp(LocalDateTime.now());
        entity.setPrediction(prediction);
        entity.setCarModel(userDTO.getCarModel());
        entity.setUserEmail(userDTO.getEmail());
        entity.setVehicleNumber(userDTO.getVehicleNumber());
        entity.setUserFullName(userDTO.getFullName());

        sensorReadingsRepository.save(entity);
    }

    private double generateRandomValue(double min, double max) {
        return Math.round((min + (max - min) * Math.random()) * 100.0) / 100.0;
    }
}