package gp.project.alertService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "gp.project.alertService.service.client")
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class AlertServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertServiceApplication.class, args);
	}

	@Bean
	public JsonMessageConverter jsonMessageConverter() {
		return new ByteArrayJsonMessageConverter();
	}
}