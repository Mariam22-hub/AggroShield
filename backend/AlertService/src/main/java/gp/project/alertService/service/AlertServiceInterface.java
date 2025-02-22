package gp.project.alertService.service;

import gp.project.alertService.dto.AlertDTO;

public interface AlertServiceInterface {
    public void consume(AlertDTO alertDTO);
}