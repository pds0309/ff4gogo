package com.pds.schedule;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ScheduleApplication {
    public static void main(String[] args) {
        if (System.getProperty("spring.profiles.default") == null) {
            System.setProperty("spring.profiles.default", "local");
            System.setProperty("spring.profiles.active", "local");
        }
        SpringApplication.run(ScheduleApplication.class, args);
    }
}
