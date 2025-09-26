package com.sporty.airport.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache")
@Data
public class CacheProperties {

    /**
     * Cache name
     */
    private String name;

    /**
     * Cache size
     */
    private int maxSize;

    /**
     * Cache expiration time in hours.
     */
    private int expireHours;
}