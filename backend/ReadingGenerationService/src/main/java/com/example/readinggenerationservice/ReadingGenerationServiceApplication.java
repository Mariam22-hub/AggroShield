package com.example.readinggenerationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.readinggenerationservice.service.client")
public class ReadingGenerationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadingGenerationServiceApplication.class, args);
    }

}