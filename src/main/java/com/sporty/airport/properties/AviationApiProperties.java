package com.sporty.airport.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aviationapi")
@Data
public class AviationApiProperties {
    /**
     * Base URL of the Aviation API.
     */
    private String baseUrl;

    /**
     * Request timeout in seconds for API calls.
     */
    private int timeoutSeconds;

    /**
     * Number of retry attempts for failed requests.
     */
    private int retries;

    /**
     * Initial backoff duration in milliseconds between retries.
     */
    private int backoffMillis;

    /**
     * Maximum backoff duration in milliseconds for retries.
     */
    private int maxBackoffMillis;
}