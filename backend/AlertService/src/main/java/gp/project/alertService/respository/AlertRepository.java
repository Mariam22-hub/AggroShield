package gp.project.alertService.respository;

import gp.project.alertService.model.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    public List<AlertEntity> findByUserEmail(String userEmail);
}