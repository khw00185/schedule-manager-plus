package com.example.schedulemanagerplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ScheduleManagerPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleManagerPlusApplication.class, args);
    }

}
