package com.pds.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) {
        if (System.getProperty("spring.profiles.default") == null) {
            System.setProperty("spring.profiles.default", "local");
            System.setProperty("spring.profiles.active", "local");
        }
        SpringApplication.run(WebApplication.class, args);
    }
}
