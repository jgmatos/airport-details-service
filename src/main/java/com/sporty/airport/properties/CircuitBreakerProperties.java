package com.sporty.airport.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resilience")
@Data
public class CircuitBreakerProperties {

    /**
     * Circuit breaker name
     */
    private String name;

    /**
     * Failure rate threshold
     */
    private int failureRate;

    /**
     * Wait duration in open state, seconds
     */
    private int waitSeconds;

    /**
     * Sliding window size
     */
    private int window;

}