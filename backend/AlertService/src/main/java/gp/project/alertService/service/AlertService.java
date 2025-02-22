package gp.project.alertService.service;

import com.example.shared.DTO.UserDTO;
import gp.project.alertService.dto.AlertDTO;
import gp.project.alertService.model.AlertEntity;
import gp.project.alertService.mapper.AlertsMapper;
import gp.project.alertService.respository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService implements AlertServiceInterface {
    private final AlertRepository repository;
    private final RedisTemplate<String, String> redisTemplate;

    private String fullkey;
    private static final String REDIS_KEY_PREFIX = "alerts:driver:";
    private final ConcurrentHashMap<String, UserDTO> userCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> redisEvent = new ConcurrentHashMap<>();

    @Qualifier("gp.project.alertService.service.client.UserClient")

    @KafkaListener(topics = "behaviour-alert")
    public void consume(AlertDTO alertDTO) {
        log.info("Received alert: {}", alertDTO);
        log.info("Received alert: {}", alertDTO.getUserDTO());

        // Fetch user details and prepare Redis storage
        String redisKey = generateKeyForUser(alertDTO.getUserDTO().getEmail());

        userCache.put(redisKey, alertDTO.getUserDTO());
        redisEvent.put("event", alertDTO.getDescription());
        storeEventInRedis(redisKey, alertDTO.getUserDTO(), alertDTO);
    }

    /**
     * Generate a Redis key that includes the current 10-minute window start time.
     */
    private String generateKeyForUser(String driverEmail) {
        fullkey = REDIS_KEY_PREFIX + driverEmail;
        return REDIS_KEY_PREFIX + driverEmail;
    }

    /**
     * Store alert data in Redis. If it's the first alert in this window, save user details as well.
     */
    private void storeEventInRedis(String redisKey, UserDTO userDTO, AlertDTO alertDTO) {
        boolean isNewKey = Boolean.FALSE.equals(redisTemplate.hasKey(redisKey));

        if (isNewKey) {
            // First event: store user details and set TTL for 10 minutes
            storeUserDetailsInRedis(redisKey, userDTO);
            redisTemplate.expire(redisKey, Duration.ofMinutes(1));
            redisTemplate.expire(redisKey + ":events", Duration.ofMinutes(1));
            log.info("Created new Redis key {} with a 10-minute TTL.", redisKey);
        }

        // Append event description to the list
        redisTemplate.opsForList().rightPush(redisKey + ":events", alertDTO.getDescription());
        log.info("Event added to Redis for driver: {}", userDTO.getEmail());
    }

    /**
     * Store user details in Redis for the given key.
     */
    private void storeUserDetailsInRedis(String redisKey, UserDTO userDTO) {
        redisTemplate.opsForHash().put(redisKey, "userEmail", userDTO.getEmail());
        redisTemplate.opsForHash().put(redisKey, "driverFullName", userDTO.getFullName());
        redisTemplate.opsForHash().put(redisKey, "vehicleNumber", userDTO.getVehicleNumber());
        redisTemplate.opsForHash().put(redisKey, "carModel", userDTO.getCarModel());
    }

    @Scheduled(fixedRate = 30000) // Run every minute
    public void processExpiredAlerts() {
        log.info("Checking for expired alert keys...");

        if (fullkey == null) {
            log.info("No active Redis key to process.");
            return;
        }

        Long ttl = redisTemplate.getExpire(fullkey);
        log.info(String.valueOf(ttl));

        if (ttl != null && ttl <= 0) {
            log.info("Processing expired Redis key: {}", fullkey);
            processAndSendCombinedAlert(fullkey);
            cleanUpRedisKey(fullkey);
            userCache.remove(fullkey);
        }
        else {
            log.info("Redis key is still active: {}", fullkey);
        }
    }

    private void processAndSendCombinedAlert(String redisKey) {
        log.info(userCache.toString());

        String email = userCache.get(redisKey).getEmail();
        String fullName = userCache.get(redisKey).getFullName();
        String vehicleNumber = userCache.get(redisKey).getVehicleNumber();
        String carModel = userCache.get(redisKey).getCarModel();
        String description = redisEvent.get("event");

        AlertEntity alertEntity = AlertsMapper.mapToAlertsEntity(new AlertEntity(),
                AlertsMapper.createAlertDTO(description, email, fullName, vehicleNumber, carModel));

        log.info("Aggregated events for {}: {}", email, redisEvent.get("event"));
        // Save to DB and send email
        saveAndNotify(alertEntity);
    }

    private void saveAndNotify(AlertEntity alertEntity) {
        repository.save(alertEntity);
        log.info("Aggregated alert saved for user: {}", alertEntity.getUserEmail());

    }

    private void cleanUpRedisKey(String redisKey) {
        redisTemplate.delete(redisKey);
        redisTemplate.delete(redisKey + ":events");
        log.info("Cleaned up Redis keys for {}", redisKey);
    }


}