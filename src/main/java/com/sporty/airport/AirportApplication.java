package com.sporty.airport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AirportApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirportApplication.class, args);
    }
}