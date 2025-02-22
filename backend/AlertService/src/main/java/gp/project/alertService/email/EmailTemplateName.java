package gp.project.alertService.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ALERT_NOTIFICATION("alert_notification");


    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}