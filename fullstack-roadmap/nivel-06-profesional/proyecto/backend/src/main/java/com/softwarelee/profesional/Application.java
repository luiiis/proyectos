package com.softwarelee.profesional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Activa tareas programadas (@Scheduled)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
