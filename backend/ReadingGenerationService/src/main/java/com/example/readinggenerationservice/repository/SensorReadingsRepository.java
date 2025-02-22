package com.example.readinggenerationservice.repository;

import com.example.readinggenerationservice.entity.SensorReadingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorReadingsRepository extends JpaRepository<SensorReadingsEntity, Long> {
    List<SensorReadingsEntity> findByCarNumber(String carNumber);

}